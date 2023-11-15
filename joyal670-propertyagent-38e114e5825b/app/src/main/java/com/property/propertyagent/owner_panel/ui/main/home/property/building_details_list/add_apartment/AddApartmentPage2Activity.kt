package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.add_apartment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityAddBuildingPage2Binding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.floor_image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.video_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.viewmodel.OwnerAddBuildingViewModel
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.getMediaPath
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_add_property_page3_.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class AddApartmentPage2Activity : BaseActivity1<ActivityAddBuildingPage2Binding>() {
    private lateinit var ownerAddBuildingViewModel: OwnerAddBuildingViewModel

    var propertyBuildingId = ""
    var propertyTypeId = ""
    var propertyName = ""
    var propertyStatus = ""
    var propertyAmenities = ArrayList<Int>()
    var propertyDetailsId = ArrayList<Int>()
    var propertyDetailsValue = ArrayList<Int>()
    var propertyFurnishedId = 0
    var propertyDescription = ""
    var propertyExpectedAmount = ""

    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null
    private var floorImages: ArrayList<String?>? = null
    private var floorImagesList: ArrayList<Uri?>? = null
    private var videos: ArrayList<Uri?>? = null
    private var videosList: ArrayList<String> = ArrayList()

    private val PICK_IMAGES_CODE = 0
    private val PICK_FLOOR_IMAGES_CODE = 2
    private val PICK_VIDEO_CODE = 1
    private lateinit var image_adapter: image_upload_adapter
    private lateinit var floor_image_adapter: floor_image_upload_adapter
    private lateinit var video_adapter: video_upload_adapter

    private lateinit var path: String


    override val layoutId: Int
        get() = R.layout.activity_add_building_page_2
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        propertyBuildingId = intent.extras!!.get("propertyBuildingId").toString()
        propertyTypeId = intent.extras!!.get("propertyTypeId").toString()
        propertyName = intent.extras!!.get("propertyName").toString()
        propertyStatus = intent.extras!!.get("propertyStatus").toString()

        propertyAmenities = intent.extras?.getIntegerArrayList("propertyAmenities")!!
        propertyDetailsId = intent.extras?.getIntegerArrayList("propertyDetailsId")!!
        propertyDetailsValue = intent.extras?.getIntegerArrayList("propertyDetailsValue")!!

        propertyFurnishedId = intent.extras?.getInt("propertyFurnishedId")!!
        propertyDescription = intent.extras?.getString("propertyDescription").toString()
        propertyExpectedAmount = intent.extras?.getString("propertyExpectedAmount").toString()

        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t" + getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_bar_chart_2)

        images = ArrayList()
        imagesList = ArrayList()
        videos = ArrayList()
        floorImages = ArrayList()
        floorImagesList = ArrayList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.recycerviewImageUpload.layoutManager = gridLayoutManager
        image_adapter = image_upload_adapter(this, imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            image_adapter.notifyDataSetChanged()
        }
        binding.recycerviewImageUpload.adapter = image_adapter


        val gridLayoutManager1 = GridLayoutManager(this, 3)
        gridLayoutManager1.orientation = GridLayoutManager.VERTICAL
        binding.recycerviewVideoUpload.layoutManager = gridLayoutManager1
        video_adapter = video_upload_adapter(this, videos!!) {
            videos!!.removeAt(it)
            videosList.removeAt(it)
            video_adapter.notifyDataSetChanged()
        }
        binding.recycerviewVideoUpload.adapter = video_adapter


        val gridLayoutManager3 = GridLayoutManager(this, 3)
        gridLayoutManager3.orientation = GridLayoutManager.VERTICAL
        binding.recycerviewFloorImageUpload.layoutManager = gridLayoutManager3
        floor_image_adapter = floor_image_upload_adapter(this, floorImagesList!!) {
            floorImages!!.removeAt(it)
            floorImagesList!!.removeAt(it)
            floor_image_adapter.notifyDataSetChanged()
        }
        binding.recycerviewFloorImageUpload.adapter = floor_image_adapter
    }

    override fun setupViewModel() {

        ownerAddBuildingViewModel = OwnerAddBuildingViewModel(this)
    }

    override fun setupObserver() {

        ownerAddBuildingViewModel.getAddApartmentResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    startActivity(Intent(this, HomeOwnerActivity::class.java))
                    finish()
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })
    }

    override fun onClicks() {

        binding.addPropertyPage3backBtn.setOnClickListener {
            super.onBackPressed()
        }

        binding.propertyPage3ImagepickBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }

        binding.propertyPage3VideopickBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openVideoFileChooser()
                }
            }
        }

        binding.propertyPage3ImageUploadBtn.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    openFloorImageFileChooser()
                }
            }
        }

        binding.submitBtn.setOnClickListener {
            if (images!!.size == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.please_add_atleast_one_property_images),
                    Toaster.State.ERROR,
                    Toast.LENGTH_SHORT
                )
            } else {
                ownerAddBuildingViewModel.addApartmentOwnerAddProperty(
                    propertyBuildingId,
                    propertyTypeId,
                    propertyName,
                    propertyStatus,
                    propertyAmenities,
                    propertyDetailsId,
                    propertyDetailsValue,
                    propertyFurnishedId,
                    propertyDescription,
                    propertyExpectedAmount,
                    images!!,
                    floorImages,
                    videosList
                )
            }
        }

    }

    override fun getViewBinging(): ActivityAddBuildingPage2Binding =
        ActivityAddBuildingPage2Binding.inflate(layoutInflater)

    private fun openFloorImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(intent, "Select Image(s)"),
            PICK_FLOOR_IMAGES_CODE
        )
    }

    private fun openImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    private fun openVideoFileChooser() {
        val intent = Intent()
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Video(s)"), PICK_VIDEO_CODE)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                        imagesList!!.add(imageUri)
                        image_adapter.notifyDataSetChanged()
                    }
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                    imagesList!!.add(imageUri)
                    image_adapter.notifyDataSetChanged()

                }
            }
        } else if (requestCode == PICK_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri
                        videos!!.add(videoUri)
                        processVideo(videoUri)
                        video_adapter.notifyDataSetChanged()
                    }
                } else {
                    val videoUri = data.data
                    videos!!.add(videoUri)
                    processVideo(videoUri)
                    video_adapter.notifyDataSetChanged()
                }
            }
        } else if (requestCode == PICK_FLOOR_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val FloorUri = data.clipData!!.getItemAt(i).uri
                        floorImages!!.add(CommonUtils.getImageRealPath(FloorUri, this))
                        floorImagesList!!.add(FloorUri)
                        floor_image_adapter.notifyDataSetChanged()
                    }
                } else {
                    val FloorUri = data.data
                    floorImages!!.add(CommonUtils.getImageRealPath(FloorUri, this))
                    floorImagesList!!.add(FloorUri)
                    floor_image_adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun processVideo(uri: Uri?) {
        uri?.let {
            GlobalScope.launch {
                // run in background as it can take a long time if the video is big,
                // this implementation is not the best way to do it,
                // todo(abed): improve threading
                val job = async { getMediaPath(applicationContext, uri) }
                path = job.await()

                val desFile = saveVideoFile(path)

                desFile?.let {
                    var time = 0L
                    VideoCompressor.start(
                        path,
                        desFile.path,
                        object : CompressionListener {
                            override fun onProgress(percent: Float) {
                                //Update UI
                                if (percent <= 100 && percent.toInt() % 5 == 0)
                                    runOnUiThread {

                                    }
                            }

                            override fun onStart() {
                                time = System.currentTimeMillis()
                                showProgressOwner()
                            }

                            override fun onSuccess() {
                                val newSizeValue = desFile.length()
                                time = System.currentTimeMillis() - time
                                path = desFile.path
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        dismissProgressOwner()
                                        videosList.add(path)
                                    }, 50)
                                }
                            }

                            override fun onFailure(failureMessage: String) {
                                dismissProgressOwner()
                                Toaster.showToast(
                                    this@AddApartmentPage2Activity,
                                    failureMessage,
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                                Log.wtf("failureMessage", failureMessage)
                            }

                            override fun onCancelled() {
                                Toaster.showToast(
                                    this@AddApartmentPage2Activity,
                                    "compression has been cancelled",
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                                // make UI changes, cleanup, etc
                            }
                        },
                        VideoQuality.MEDIUM,
                        isMinBitRateEnabled = false,
                        keepOriginalResolution = false
                    )
                }
            }
        }
    }

    private fun saveVideoFile(filePath: String?): File? {
        filePath?.let {
            val videoFile = File(filePath)
            val videoFileName = "${System.currentTimeMillis()}_${videoFile.name}"
            val folderName = Environment.DIRECTORY_MOVIES
            if (Build.VERSION.SDK_INT >= 30) {

                val values = ContentValues().apply {

                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        videoFileName
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val collection =
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val fileUri = applicationContext.contentResolver.insert(collection, values)

                fileUri?.let {
                    application.contentResolver.openFileDescriptor(fileUri, "rw")
                        .use { descriptor ->
                            descriptor?.let {
                                FileOutputStream(descriptor.fileDescriptor).use { out ->
                                    FileInputStream(videoFile).use { inputStream ->
                                        val buf = ByteArray(4096)
                                        while (true) {
                                            val sz = inputStream.read(buf)
                                            if (sz <= 0) break
                                            out.write(buf, 0, sz)
                                        }
                                    }
                                }
                            }
                        }

                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    applicationContext.contentResolver.update(fileUri, values, null, null)

                    return File(getMediaPath(applicationContext, fileUri))
                }
            } else {
                val downloadsPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(downloadsPath, videoFileName)

                if (desFile.exists())
                    desFile.delete()

                try {
                    desFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return desFile
            }
        }
        return null
    }

}
package com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
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
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.floor_image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.video_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel.AddPropertyViewModel
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
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


class AddPropertyPage3 : BaseActivity() {
    private lateinit var addPropertyViewModel: AddPropertyViewModel

    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null
    private var floorImages: ArrayList<String?>? = null
    private var floorImagesList: ArrayList<Uri?>? = null
    private var videos: ArrayList<Uri?>? = null
    private var videosList: ArrayList<String> = ArrayList()
    private var position = 0
    private val PICK_IMAGES_CODE = 0
    private val PICK_FLOOR_IMAGES_CODE = 2
    private val PICK_VIDEO_CODE = 1
    private lateinit var image_adapter: image_upload_adapter
    private lateinit var floor_image_adapter: floor_image_upload_adapter
    private lateinit var video_adapter: video_upload_adapter

    private lateinit var path: String

    private var propertyMainType = ""
    private var propertyRentBuyType = ""
    private var propertyResidentialCommercialType = ""
    private var propertyName = ""
    private var propertyStreetAddress1 = ""
    private var propertyStreetAddress2 = ""
    private var propertyTypeID = ""
    private var propertyCountryId = ""
    private var propertyStateId = ""
    private var propertyCityId = ""
    private var propertyZipCodeId = ""

    private var propertyStatus = ""
    private var amenitiesList = ArrayList<Int>()
    private var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()
    private var furnishedId = 0
    private var propertyDescription = ""
    private var propertyExpectedAmount = ""
    private var propertyLng = ""
    private var propertyLat = ""

    private lateinit var dialog: ProgressDialog


    override val layoutId: Int
        get() = R.layout.activity_add_property_page3_
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t" + getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_bar_chart_2)

        images = ArrayList()
        imagesList = ArrayList()
        videos = ArrayList()
        floorImages = ArrayList()
        floorImagesList = ArrayList()


        propertyMainType = intent.extras!!.get("propertyMainType").toString()
        propertyRentBuyType = intent.extras!!.get("propertyRentBuyType").toString()
        propertyResidentialCommercialType =
            intent.extras!!.get("propertyResidentialCommercialType").toString()
        propertyName = intent.extras!!.get("propertyName").toString()
        propertyStreetAddress1 = intent.extras!!.get("propertyStreetAddress1").toString()
        propertyStreetAddress2 = intent.extras!!.get("propertyStreetAddress2").toString()
        propertyTypeID = intent.extras!!.get("propertyTypeID").toString()
        propertyCountryId = intent.extras!!.get("propertyCountryId").toString()
        propertyStateId = intent.extras!!.get("propertyStateId").toString()
        propertyCityId = intent.extras!!.get("propertyCityId").toString()
        propertyZipCodeId = intent.extras!!.get("propertyZipCodeId").toString()

        propertyStatus = intent.extras!!.get("propertyStatus").toString()
        amenitiesList = intent.extras?.getIntegerArrayList("propertyAmenities")!!
        idList = intent.extras?.getIntegerArrayList("propertyDetailsId")!!
        valueList = intent.extras?.getIntegerArrayList("propertyDetailsValue")!!
        furnishedId = intent.extras?.getInt("propertyFurnishedId")!!
        propertyDescription = intent.extras?.getString("propertyDescription").toString()
        propertyExpectedAmount = intent.extras?.getString("propertyExpectedAmount").toString()
        propertyLng = intent.extras!!.get("propertyLng").toString()
        propertyLat = intent.extras!!.get("propertyLat").toString()

    }

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
                    position = 0
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                    imagesList!!.add(imageUri)
                    image_adapter.notifyDataSetChanged()
                    position = 0

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
                    position = 0
                } else {
                    val videoUri = data.data
                    videos!!.add(videoUri)
                    processVideo(videoUri)
                    video_adapter.notifyDataSetChanged()
                    position = 0
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
                    position = 0
                } else {
                    val FloorUri = data.data
                    floorImages!!.add(CommonUtils.getImageRealPath(FloorUri, this))
                    floorImagesList!!.add(FloorUri)
                    floor_image_adapter.notifyDataSetChanged()
                    position = 0
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recycerview_image_upload.layoutManager = gridLayoutManager
        image_adapter = image_upload_adapter(this, imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            image_adapter.notifyDataSetChanged()
        }
        recycerview_image_upload.adapter = image_adapter


        val gridLayoutManager1 = GridLayoutManager(this, 3)
        gridLayoutManager1.orientation = GridLayoutManager.VERTICAL
        recycerview_video_upload.layoutManager = gridLayoutManager1
        video_adapter = video_upload_adapter(this, videos!!) {
            videos!!.removeAt(it)
            videosList.removeAt(it)
            video_adapter.notifyDataSetChanged()
        }
        recycerview_video_upload.adapter = video_adapter


        val gridLayoutManager3 = GridLayoutManager(this, 3)
        gridLayoutManager3.orientation = GridLayoutManager.VERTICAL
        recycerview_floorImage_upload.layoutManager = gridLayoutManager3
        floor_image_adapter = floor_image_upload_adapter(this, floorImagesList!!) {
            floorImages!!.removeAt(it)
            floorImagesList!!.removeAt(it)
            floor_image_adapter.notifyDataSetChanged()
        }
        recycerview_floorImage_upload.adapter = floor_image_adapter
    }

    override fun setupViewModel() {
        addPropertyViewModel = AddPropertyViewModel(this)
    }

    override fun setupObserver() {

        addPropertyViewModel.addApartmentOwnerPropertyData.observe(this, Observer {
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
        property_page3_imagepickBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }

        property_page3_videopickBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openVideoFileChooser()
                }
            }
        }

        add_property_page3backBtn.setOnClickListener {
            super.onBackPressed()
        }

        property_page3_imageUploadBtn.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    openFloorImageFileChooser()
                }
            }
        }

        submitBtn.setOnClickListener {

            if (images!!.size == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.please_add_atleast_one_property_images),
                    Toaster.State.ERROR,
                    Toast.LENGTH_SHORT
                )
            } else {
                addPropertyViewModel.addApartmentOwnerAddProperty(
                    propertyMainType,
                    propertyRentBuyType,
                    propertyResidentialCommercialType,
                    propertyName,
                    propertyStreetAddress1,
                    propertyStreetAddress2,
                    propertyTypeID,
                    propertyCountryId,
                    propertyStateId,
                    propertyCityId,
                    propertyZipCodeId,

                    propertyStatus,
                    images,
                    floorImages,
                    videosList,
                    amenitiesList,
                    idList,
                    valueList,
                    furnishedId,
                    propertyDescription,
                    propertyExpectedAmount,
                    propertyLng,
                    propertyLat
                )
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
                                dialog = ProgressDialog.show(
                                    this@AddPropertyPage3, "Processing",
                                    "Loading. Please wait...", true
                                )
                            }

                            override fun onSuccess() {
                                val newSizeValue = desFile.length()
                                time = System.currentTimeMillis() - time
                                path = desFile.path
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        //dismissProgressOwner()
                                        dialog.dismiss()
                                        videosList.add(path)
                                    }, 50)
                                }
                            }

                            override fun onFailure(failureMessage: String) {
                                try {
                                    dialog.dismiss()
                                    Toaster.showToast(
                                        this@AddPropertyPage3,
                                        failureMessage,
                                        Toaster.State.ERROR,
                                        Toast.LENGTH_LONG
                                    )
                                    Log.wtf("failureMessage", failureMessage)
                                } catch (ex: Exception) {

                                }
                            }

                            override fun onCancelled() {
                                try {
                                    dialog.dismiss()
                                    Toaster.showToast(
                                        this@AddPropertyPage3,
                                        "compression has been cancelled",
                                        Toaster.State.ERROR,
                                        Toast.LENGTH_LONG
                                    )
                                } catch (ex: Exception) {

                                }

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
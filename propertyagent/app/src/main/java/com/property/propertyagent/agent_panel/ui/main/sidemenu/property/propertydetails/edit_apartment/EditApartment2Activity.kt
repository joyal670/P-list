package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.edit_apartment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.PropertyDetailsEditViewModel
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.*
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.PropertyDetailsPage1Activity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_assigned_property_details.Document
import com.property.propertyagent.modal.agent.agent_assigned_property_details.FloorPlan
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import kotlinx.android.synthetic.main.activity_property_details_page3.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

@Suppress("PrivatePropertyName")
class EditApartment2Activity : BaseActivity() {

    private lateinit var propertyDetailsEditViewModel: PropertyDetailsEditViewModel
    private var images: ArrayList<String>? = null
    private var videos: ArrayList<Uri>? = null
    private var passedImages: ArrayList<Document>? = null
    private var passedFloorPlan: ArrayList<FloorPlan>? = null
    private var position = 0
    private val PICK_IMAGES_CODE = 0
    private val PICK_VIDEO_CODE = 1
    private lateinit var agentVideoFromApiAdapter: AgentVideoFromApiAdapter
    private lateinit var imageAdapter: AgentImageUploadAdapter
    private lateinit var videoAdapter: AgentVideoUploadAdapter
    private lateinit var imageAdapterFromApi: AgentImageFromApiAdapter
    private lateinit var agentFloorImageAdapter: AgentFloorImageUploadAdapter
    private lateinit var agentFloorImageAdapterFromApi: AgentFloorImageFromApiAdapter

    private var sortedImages: ArrayList<Document>? = null
    private var sortedVideos: ArrayList<Document>? = null

    private var imagesList: ArrayList<Uri?>? = null
    private var floorImages: ArrayList<String>? = null
    private var floorImagesList: ArrayList<Uri?>? = null
    private var videosList: ArrayList<String> = ArrayList()
    private val PICK_FLOOR_IMAGES_CODE = 2

    private var unitId = 0
    private var spinnerType = ""
    private var occupiedStatus = ""
    private var pDesc = ""

    private var isVideoDeleted: Boolean = false

    private var expectedAmount = ""

    private var amenitiesList = ArrayList<Int>()
    private lateinit var path: String

    var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()
    private var furnishedId = -1
    private var deleteImagePosition = -1
    private var deleteFloorImagePosition = -1

    override val layoutId: Int
        get() = R.layout.activity_edit_apartment2
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.property_details)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        images = ArrayList()
        imagesList = ArrayList()
        videos = ArrayList()
        floorImages = ArrayList()
        floorImagesList = ArrayList()

        sortedImages = ArrayList()
        sortedVideos = ArrayList()

        passedImages = ArrayList()
        passedFloorPlan = ArrayList()

        unitId = intent.extras?.getInt("UNIT_ID")!!
        expectedAmount = intent.getStringExtra("EXPECTED_AMOUNT")!!
        occupiedStatus = intent.getStringExtra("OCCUPIED_STATUS")!!
        spinnerType = intent.extras?.getString("ADD_PROPERTY_TYPE_NAME")!!
        amenitiesList = intent.extras?.getIntegerArrayList("ADD_PROPERTY_AMENITIES")!!
        idList = intent.extras?.getIntegerArrayList("ADD_PROPERTY_DETAILS_ID")!!
        valueList = intent.extras?.getIntegerArrayList("ADD_PROPERTY_DETAILS_VALUE")!!
        furnishedId = intent.extras?.getInt("ADD_PROPERTY_FURNISHED_ID")!!
        pDesc = intent.extras?.getString("ADD_PROPERTY_DES")!!
        passedImages = intent.getParcelableArrayListExtra("passed_images")!!
        passedFloorPlan = intent.getParcelableArrayListExtra("passed_floor_plans")!!

        if (passedImages!!.isNotEmpty()) {
            for (c in 0 until passedImages!!.size) {
                if (passedImages!![c].type == 0) {
                    sortedImages!!.add(passedImages!![c])
                } else {
                    sortedVideos!!.add(passedImages!![c])
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recycerview_image_upload_agent.layoutManager = gridLayoutManager
        imageAdapter = AgentImageUploadAdapter(imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
        }
        recycerview_image_upload_agent.adapter = imageAdapter

        if (sortedImages?.size!! > 0) {
            recycerview_image_upload_agentFromApi.visibility = View.VISIBLE
            val gridLayoutManagerForApi = GridLayoutManager(this, 3)
            gridLayoutManagerForApi.orientation = GridLayoutManager.VERTICAL
            recycerview_image_upload_agentFromApi.layoutManager = gridLayoutManagerForApi
            imageAdapterFromApi = AgentImageFromApiAdapter(sortedImages!!) { it, id ->
                deleteImagePosition = it
                isVideoDeleted = false
                propertyDetailsEditViewModel.agentRemovePropertyDocument(id.toString())
                Log.e("CLICK POS", "initData: $it - $id")
            }
            recycerview_image_upload_agentFromApi.adapter = imageAdapterFromApi
        }

        val gridLayoutManager1 = GridLayoutManager(this, 3)
        gridLayoutManager1.orientation = GridLayoutManager.VERTICAL
        recycerview_video_upload_agent.layoutManager = gridLayoutManager1
        videoAdapter = AgentVideoUploadAdapter(videos!!) {
            videos!!.removeAt(it)
            videoAdapter.notifyDataSetChanged()
        }
        recycerview_video_upload_agent.adapter = videoAdapter

        if (sortedVideos!!.isNotEmpty()) {
            recycerview_video_upload_agentFromApi.isVisible = true
            val gridLayoutManager1ForApi = GridLayoutManager(this, 3)
            gridLayoutManager1ForApi.orientation = GridLayoutManager.VERTICAL
            recycerview_video_upload_agentFromApi.layoutManager = gridLayoutManager1ForApi
            agentVideoFromApiAdapter = AgentVideoFromApiAdapter(sortedVideos!!) { it, id ->
                deleteImagePosition = it
                isVideoDeleted = true
                propertyDetailsEditViewModel.agentRemovePropertyDocument(id.toString())
                Log.e("CLICK POS", "initData: $it - $id")
            }
            recycerview_video_upload_agentFromApi.adapter = agentVideoFromApiAdapter
        }

        val gridLayoutManager3 = GridLayoutManager(this, 3)
        gridLayoutManager3.orientation = GridLayoutManager.VERTICAL
        recycerview_floorImage_upload_agent.layoutManager = gridLayoutManager3
        agentFloorImageAdapter = AgentFloorImageUploadAdapter(floorImagesList!!) {
            floorImages!!.removeAt(it)
            floorImagesList!!.removeAt(it)
            agentFloorImageAdapter.notifyDataSetChanged()
        }
        recycerview_floorImage_upload_agent.adapter = agentFloorImageAdapter

        if (passedFloorPlan?.size!! > 0) {
            recycerview_floorImage_upload_form_api.visibility = View.VISIBLE
            val gridLayoutManager3ForApi = GridLayoutManager(this, 3)
            gridLayoutManager3ForApi.orientation = GridLayoutManager.VERTICAL
            recycerview_floorImage_upload_form_api.layoutManager = gridLayoutManager3ForApi
            agentFloorImageAdapterFromApi =
                AgentFloorImageFromApiAdapter(passedFloorPlan!!) { it, id ->
                    deleteFloorImagePosition = it
                    propertyDetailsEditViewModel.agentRemovePropertyFloorDocument(id.toString())
                }
            recycerview_floorImage_upload_form_api.adapter = agentFloorImageAdapterFromApi
        }
    }

    override fun setupViewModel() {
        propertyDetailsEditViewModel = PropertyDetailsEditViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        propertyDetailsEditViewModel.getAgentPropertyRemoveResponse()
            .observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (isVideoDeleted) {
                            sortedVideos!!.removeAt(deleteImagePosition)
                            agentVideoFromApiAdapter.notifyDataSetChanged()
                        } else {
                            sortedImages!!.removeAt(deleteImagePosition)
                            imageAdapterFromApi.notifyDataSetChanged()
                        }
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
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
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            })
        propertyDetailsEditViewModel.getAgentPropertyFloorRemoveResponse()
            .observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        passedFloorPlan!!.removeAt(deleteFloorImagePosition)
                        agentFloorImageAdapterFromApi.notifyDataSetChanged()
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
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
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            })

        propertyDetailsEditViewModel.agentUpdateApartmentData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    val intent = Intent(this, PropertyDetailsPage1Activity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
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
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })
    }

    override fun onClicks() {
        agentproperty_page3backBtn.setOnClickListener {
            super.onBackPressed()
        }

        property_page3_imagepickBtn_agent.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }

        property_page3_videopickBtn_agent.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openVideoFileChooser()
                }
            }
        }
        property_page3_imageUploadBtnAgent.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    openFloorImageFileChooser()
                }
            }
        }
        agentproperty_page3nextBtn.setOnClickListener {

            Log.e("images", Gson().toJson(images))
            Log.e("floorImages", Gson().toJson(floorImages))
            Log.e("videosList", Gson().toJson(videosList))
            Log.e("amitiesList", Gson().toJson(amenitiesList))
            Log.e("idList", Gson().toJson(idList))
            Log.e("valueList", Gson().toJson(valueList))

            Log.e("furnishedId", furnishedId.toString())
            Log.e("pDesc", pDesc)
            Log.e("id", unitId.toString())
            Log.e("fId", furnishedId.toString())
            Log.e("OStatus", occupiedStatus)

            propertyDetailsEditViewModel.agentUpdateApartment(
                this,
                unitId.toString(),
                clicked_property_id,
                images,
                floorImages,
                videosList,
                amenitiesList,
                idList,
                valueList,
                furnishedId,
                occupiedStatus,
                pDesc,
                expectedAmount,
            )
        }
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                        images!!.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                    position = 0
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
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
                        videoAdapter.notifyDataSetChanged()
                    }
                    position = 0
                } else {
                    val videoUri = data.data
                    videoUri?.let { videos!!.add(it) }
                    processVideo(videoUri)
                    videoAdapter.notifyDataSetChanged()
                    position = 0
                }
            }
        } else if (requestCode == PICK_FLOOR_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val floorUri = data.clipData!!.getItemAt(i).uri
                        floorImages!!.add(CommonUtils.getImageRealPath(floorUri, this).toString())
                        floorImagesList!!.add(floorUri)
                        agentFloorImageAdapter.notifyDataSetChanged()
                    }
                    position = 0
                } else {
                    val floorUri = data.data
                    floorImages!!.add(CommonUtils.getImageRealPath(floorUri, this).toString())
                    floorImagesList!!.add(floorUri)
                    agentFloorImageAdapter.notifyDataSetChanged()
                    position = 0
                }
            }
        }
    }

    private fun processVideo(uri: Uri?) {
        uri?.let {
            GlobalScope.launch {
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
                                showProgress()
                            }

                            override fun onSuccess() {
                                time = System.currentTimeMillis() - time
                                path = desFile.path
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        dismissProgress()
                                        videosList.add(path)
                                    }, 50)
                                }
                            }

                            override fun onFailure(failureMessage: String) {
                                dismissProgress()
                                Toaster.showToast(
                                    this@EditApartment2Activity,
                                    failureMessage,
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                                Log.wtf("failureMessage", failureMessage)
                            }

                            override fun onCancelled() {
                                Toaster.showToast(
                                    this@EditApartment2Activity,
                                    "compression has been cancelled",
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
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
                    getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
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
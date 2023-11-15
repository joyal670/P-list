package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_detailed_page.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_properties_for_service.Data
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_detailed_page.adapter.UploadServiceDocumentsAdapter
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_detailed_page.viewmodel.ServiceDetailedViewModel
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.success.ServiceSuccessActivity
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ServiceDetailedActivity : BaseActivity() {
    private lateinit var serviceDetailedViewModel : ServiceDetailedViewModel
    private var selectedId = -1
    private var selectedServiceName = ""
    private var propertiesNameList = ArrayList<String>()
    private var propertiesIdList = ArrayList<Int>()
    private var selectedPropertyId = ""

    private lateinit var imageAdapter : UploadServiceDocumentsAdapter
    private var imagesUpload : java.util.ArrayList<String?>? = null
    private var imagesList : java.util.ArrayList<Uri?>? = null
    private var position = 0

    private var checkDate = false
    private var checkTime = false

    override val layoutId : Int
        get() = R.layout.activity_service_details
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        selectedId = intent.getIntExtra(Constants.MAINTANCE_ID , 0)
        selectedServiceName = intent.getStringExtra(Constants.MAINTANCE_NAME).toString()

        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.service_name)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        imagesUpload = ArrayList()
        imagesList = ArrayList()

        tvServiceFullName.text = selectedServiceName
        serviceDetailedViewModel.ownerPropertyDetails(selectedId)

    }

    private fun setupPropertyChooseDropDown(data : List<Data>) {
        propertiesNameList = ArrayList()
        propertiesIdList = ArrayList()
        for (i in data.indices) {
            propertiesNameList.add(data[i].property_name)
            propertiesIdList.add(data[i].owner_property_id)
        }
        spPropertyChoose.setItems(propertiesNameList)
        spPropertyChoose.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedPropertyId = propertiesIdList[newIndex].toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {
        val gridLayoutManager = GridLayoutManager(this , 4)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAttachFileListsService.layoutManager = gridLayoutManager
        imageAdapter = UploadServiceDocumentsAdapter(this , imagesList!!) {
            imagesUpload!!.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
        }
        rvAttachFileListsService.adapter = imageAdapter
    }

    override fun setupViewModel() {
        serviceDetailedViewModel = ServiceDetailedViewModel()
    }

    override fun setupObserver() {
        serviceDetailedViewModel.getOwnerPropertyDetailsResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data != null) {
                        Log.e("response" , Gson().toJson(it.data.data))
                        if (!(it.data.data.isNullOrEmpty())) {
                            main_layout.visibility = View.VISIBLE
                            setupPropertyChooseDropDown(it.data.data)
                        } else {
                            noService.visibility = View.VISIBLE
                            main_layout.visibility = View.GONE

                        }
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)

                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    main_layout.visibility = View.GONE
                    showProgressOwner()
                }
            }
        })
        serviceDetailedViewModel.getOwnerUploadServiceDocumentsResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data != null) {
                        Log.e("response" , Gson().toJson(it))
                        val intent = Intent(this , ServiceSuccessActivity::class.java)
                        intent.putExtra("params", it.data.response)
                        startActivityForResult(intent , 22)
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)

                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
            }
        })

    }

    @SuppressLint("SetTextI18n")
    override fun onClicks() {
        btnChangeService.setOnClickListener {
            onBackPressed()
        }
        activity_service_requestBtn.setOnClickListener {
            when {
                service_DateTv.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this , getString(R.string.date_required) ,
                        Toaster.State.WARNING , Toast.LENGTH_LONG)
                }
                service_TimeTv.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this , getString(R.string.time_required) ,
                        Toaster.State.WARNING , Toast.LENGTH_LONG)
                }
                selectedPropertyId.isNullOrEmpty() -> {
                    Toaster.showToast(this , getString(R.string.choose_property) ,
                        Toaster.State.WARNING , Toast.LENGTH_LONG)
                }
                imagesUpload!!.isEmpty() -> {
                    Toaster.showToast(this , getString(R.string.add_images) ,
                        Toaster.State.WARNING , Toast.LENGTH_LONG)
                }
                etDescriptionService.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this , getString(R.string.description_required) ,
                        Toaster.State.WARNING , Toast.LENGTH_LONG)
                }
                else -> {
                    serviceDetailedViewModel.ownerUploadUserServiceDocumentsDetails(
                        this ,
                        selectedPropertyId ,
                        selectedId.toString() ,
                        service_DateTv.text.trim().toString() ,
                        service_TimeTv.text.trim().toString() ,
                        etDescriptionService.text.trim().toString() ,
                        imagesUpload
                    )
                }
            }
        }
        service_DateTv.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Select Card Expiry Date")
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy" , Locale.getDefault()) // modify format
                    val date = formatter.format(Date(it))

                    service_DateTv.setText(date.toString())
                    checkDate = true
                }
            }

            datePicker.show(supportFragmentManager , "MyTAG")
        }

        service_TimeTv.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            materialTimePicker.addOnPositiveButtonClickListener {
                var newHour : String = materialTimePicker.hour.toString()
                var newMinute : String = materialTimePicker.minute.toString()

                if (newHour.length == 1) {
                    newHour = "0$newHour"
                }
                if (newMinute.length == 1) {
                    newMinute = "0$newMinute"
                }
                Log.e("time" , "$newHour:$newMinute")
                checkTime = true
                service_TimeTv.setText("$newHour:$newMinute")

            }
            materialTimePicker.show(supportFragmentManager , "fragment_tag")
        }

        service_ChooseFileBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooserForDocument()
                }
            }
        }
        service_ImageChip.setOnClickListener {
            service_ImageChip.visibility = View.GONE
        }
    }

    private fun openImageFileChooserForDocument() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , true)
        intent.action = Intent.ACTION_PICK
        startForResult.launch(Intent.createChooser(intent , "Select Image(s)"))
    }

    @SuppressLint("NotifyDataSetChanged")
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent!!.clipData != null) {
                    val count = intent.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = intent.clipData!!.getItemAt(i).uri
                        imagesUpload!!.add(CommonUtils.getImageRealPath(imageUri , this))
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()

                    }
                    position = 0
                } else {
                    val imageUri = intent.data
                    imagesUpload!!.add(CommonUtils.getImageRealPath(imageUri , this))
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                    position = 0

                }
            }
        }

    override fun onActivityResult(requestCode : Int , resultCode : Int , data : Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        Log.e("onActivityResult111" , "inside $resultCode--$requestCode")
        // Check that it is the SecondActivity with an OK result
        if (requestCode == 22) {
            Log.e("inside Activity111" , "ss")
            //if (resultCode == Activity.RESULT_OK) {
            Log.e("inside Activity111" , "success")
            val intent = Intent()
            intent.putExtra("statuscode" , "success")
            setResult(Activity.RESULT_OK , intent)
            finish()
            //}
        }
    }

}
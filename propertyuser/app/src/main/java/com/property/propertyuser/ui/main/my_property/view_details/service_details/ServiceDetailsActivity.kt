package com.property.propertyuser.ui.main.my_property.view_details.service_details

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityServiceDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.maintenance.success.SuccessActivity
import com.property.propertyuser.ui.main.my_property.view_details.service_details.adapter.ImageUploadAdapter
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_service_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ServiceDetailsActivity : BaseActivity<ActivityServiceDetailsBinding>(), ActivityListener {
    private lateinit var serviceDetailsViewModel: ServiceDetailsViewModel
    override fun getViewBinging(): ActivityServiceDetailsBinding = ActivityServiceDetailsBinding.inflate(layoutInflater)
    private var images: ArrayList<Uri?>? = null
    private var imagesPassed:ArrayList<String?>?=null
    private var position = 0
    private val PICK_IMAGES_CODE = 0
    private var loadImageCode = "00"
    private lateinit var imageAdapter : ImageUploadAdapter
    private var serviceId=""
    private var serviceName=""
    private var userPropertyId=""
    private var checkDate=false
    private var checkTime=false
    private var date: String = ""
    override val layoutId: Int
        get() = R.layout.activity_service_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        serviceId=intent.getStringExtra("service_id").toString()
        serviceName=intent.getStringExtra("service_name").toString()
        userPropertyId=intent.getStringExtra("user_property_id").toString()
        setTitle(serviceName)

        tvSelectedServiceName.text=serviceName

        images= ArrayList()
        imagesPassed= ArrayList()
        setUpRecyclerViewForSavedFiles()
    }

    private fun setUpRecyclerViewForSavedFiles() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvUploadFileService.layoutManager = linearLayoutManager
        imageAdapter =
            ImageUploadAdapter(
                this,
                images!!
            ) {
                images!!.removeAt(it)
                Log.e("CLICK POS", "initData: " + it)
                imageAdapter?.notifyDataSetChanged()
            }
        rvUploadFileService.adapter = imageAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult111", "inside $resultCode--$requestCode")
        // Check that it is the SecondActivity with an OK result
        if (requestCode == 22) {
            Log.e("inside Activity111","ss")
            if (resultCode == Activity.RESULT_OK) {
                Log.e("inside Activity111","success")
                val intent = Intent()
                intent.putExtra("statuscode", "success")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        if (loadImageCode == "09")
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data!!.clipData != null)
                {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        imagesPassed!!.add(imageUri?.path)
                        images!!.add(imageUri)
                        imageAdapter?.notifyDataSetChanged()
                    }
                    position = 0
                    loadImageCode="00"
                }
                else{
                    val imageUri = data.data
                    imagesPassed!!.add(imageUri?.path)
                    images!!.add(imageUri)
                    imageAdapter?.notifyDataSetChanged()
                    position = 0
                }
            }
            else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        serviceDetailsViewModel= ServiceDetailsViewModel()
    }

    override fun setupObserver() {
        serviceDetailsViewModel.getRequestForServiceResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(this,it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                    val intent=Intent(this,SuccessActivity::class.java)
                    startActivityForResult(intent,22)
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnChange.setOnClickListener {
            onBackPressed()
        }

        tvDate.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                val calendarConstraintBuilder = CalendarConstraints.Builder()
                calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
                builder.setCalendarConstraints(calendarConstraintBuilder.build())
                val picker = builder.build()
                this?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
                picker.addOnPositiveButtonClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val formatter =
                            SimpleDateFormat("dd-MM-yyyy") // modify format
                        date = formatter.format(Date(it))
                        checkDate=true
                        tvDate.text = date
                        tvDate.setTextColor(Color.BLACK)

                        tvTime.isEnabled=true
                        checkTime=false
                        tvTime.text=getString(R.string.tvTime)
                        tvTime.setTextColor(ContextCompat.getColor(this,R.color.lightGray1))
                    }

                }
            }
        tvTime.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Select Time")
                    .setHour(12)
                    .setMinute(0)
                    .build()
            this?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                // call back code
                var newHour: String = picker.hour.toString()
                var newMinute: String = picker.minute.toString()

                val temp = Calendar.getInstance()
                temp[Calendar.HOUR_OF_DAY] = newHour.toInt()
                temp[Calendar.MINUTE] = newMinute.toInt()

                if(date == this.getCurrentDateOtherFormat()){
                    if(temp.after(GregorianCalendar.getInstance())){
                        if(newHour.length==1){
                            newHour= "0$newHour"
                        }
                        if(newMinute.length==1){
                            newMinute="0$newMinute"
                        }
                        Log.e("time", "$newHour:$newMinute")
                        checkTime=true
                        tvTime.text= "$newHour:$newMinute"
                        tvTime.setTextColor(Color.BLACK)
                    }
                    else{
                        checkTime=false
                        tvTime.text=getString(R.string.tvTime)
                        tvTime.setTextColor(ContextCompat.getColor(this,R.color.lightGray1))
                        Toaster.showToast(this, getString(R.string.cannot_select_past_time), Toaster.State.WARNING, Toast.LENGTH_SHORT)

                    }
                }
                else
                {
                    if(newHour.length==1){
                        newHour= "0$newHour"
                    }
                    if(newMinute.length==1){
                        newMinute="0$newMinute"
                    }
                    Log.e("time", "$newHour:$newMinute")
                    checkTime=true
                    tvTime.text= "$newHour:$newMinute"
                    tvTime.setTextColor(Color.BLACK)
                }
            }
        }
        btnChooseFile
            .setOnClickListener {
            ImagePicker.with(this)
                .crop()        //Crop image(Optional), Check Customization for more option
                .compress(1024)   //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
                loadImageCode="09"
        }
        btnServiceRequest.setOnClickListener {
            if(!checkDate){
                Toaster.showToast(this,getString(R.string.date_required),
                    Toaster.State.ERROR, Toast.LENGTH_LONG)
            }else if(!checkTime){
                Toaster.showToast(this,getString(R.string.time_required),
                    Toaster.State.ERROR, Toast.LENGTH_LONG)
            }else if(images!!.size<=0){
                Toaster.showToast(this,getString(R.string.upload_file),
                    Toaster.State.ERROR, Toast.LENGTH_LONG)
            }else if(etDescription.text.trim().toString().isEmpty()){
                Toaster.showToast(this,getString(R.string.description_required),
                    Toaster.State.ERROR, Toast.LENGTH_LONG)
            }else{
                serviceDetailsViewModel.uploadRequestForServiceDetails(this,serviceId,userPropertyId,
                    tvDate.text.trim().toString(),tvTime.text.trim().toString(),etDescription.text.trim().toString(),
                    imagesPassed)
            }
        }
    }


    override fun navigateToFragment(fragment: Fragment) {

    }
    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
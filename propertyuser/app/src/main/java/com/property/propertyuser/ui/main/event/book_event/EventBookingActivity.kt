package com.property.propertyuser.ui.main.event.book_event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityEventBookingBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.event_package.EventPackage
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.activity_event_booking.*
import kotlinx.android.synthetic.main.layout_no_network.*

class EventBookingActivity : BaseActivity<ActivityEventBookingBinding>(),ActivityListener {
    private lateinit var eventBookingPackageViewModel: EventBookingPackageViewModel
    private var passedPackageId=""
    private var personsCounter=MutableLiveData<Int>()
    private var totalAmount=MutableLiveData<Double>()
    private var initialAmount=0.0
    override val layoutId: Int
        get() = R.layout.activity_event_booking
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }
    private fun getCounterResponse(): LiveData<Int> {
        return personsCounter
    }
    override fun initData() {
        setTitle(getString(R.string.event_booking))
        passedPackageId=intent.getStringExtra("package_id").toString()
        eventBookingPackageViewModel.fetchEventsBookingPackagesDetails(passedPackageId)
        initialAmount=0.0
        personsCounter.postValue(0)
        totalAmount.postValue(0.0)
        tvEventBookingTotalAmountValue.text=getString(R.string.sar)+" "+
                initialAmount.toString()
        personsCounter.observe(this) {
            tvNumberofpersonValue.text=personsCounter.value.toString()
            if(personsCounter.value==0){
                totalAmount.postValue(0.0)
            }
            else if(personsCounter.value!=0){
                totalAmount.postValue(initialAmount!!*personsCounter.value!!)
            }
        }
        totalAmount.observe(this){
            tvEventBookingTotalAmountValue.text=getString(R.string.sar)+" "+totalAmount.value.toString()
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        eventBookingPackageViewModel= EventBookingPackageViewModel()
    }

    override fun setupObserver() {
        eventBookingPackageViewModel.getEventBookingPackageDetailsResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        if(it.data.response_data!=null){
                            includeNoInternetEventBooking.visibility= View.GONE
                            linearNoDataFoundEventBooking.visibility= View.GONE
                            constraintEventBookingMain.visibility= View.VISIBLE
                            if(it.data.response_data.event_packages_data!=null){
                                tvEventAmount.text=getString(R.string.sar)+" "+
                                        it.data.response_data.event_packages_data.price
                                initialAmount=it.data.response_data.event_packages_data.price.toDouble()
                                if(it.data.response_data.event_packages_data.event_package_image!=null){
                                    roundedEventBookingImage.loadImagesWithGlideExt(
                                        it.data.response_data.event_packages_data.event_package_image.package_image
                                    )
                                }
                            }
                            if(it.data.response_data.user_data!=null){
                                etEventBookingUserName.setText(it.data.response_data.user_data.name)
                                etEventBookingUserEmail.setText(it.data.response_data.user_data.email)
                                etEventBookingUserPhone.setText(it.data.response_data.user_data.phone)
                            }
                        }
                        else{
                            includeNoInternetEventBooking.visibility= View.GONE
                            linearNoDataFoundEventBooking.visibility= View.VISIBLE
                            constraintEventBookingMain.visibility= View.GONE
                        }
                    }
                    else{
                        includeNoInternetEventBooking.visibility= View.GONE
                        linearNoDataFoundEventBooking.visibility= View.VISIBLE
                        constraintEventBookingMain.visibility= View.GONE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    includeNoInternetEventBooking.visibility= View.GONE
                    linearNoDataFoundEventBooking.visibility= View.VISIBLE
                    constraintEventBookingMain.visibility= View.GONE
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetEventBooking.visibility= View.VISIBLE
                        linearNoDataFoundEventBooking.visibility= View.GONE
                        constraintEventBookingMain.visibility= View.GONE
                    }
                }

            }
        })
        eventBookingPackageViewModel.getEventBookPackageDetailsResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        Toaster.showToast(this,it.data.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                        if(it.data.event_booking_id>-1){
                            val intent= Intent(this,PaymentActivity::class.java)
                            intent.putExtra("passed_type","event_payment")
                            intent.putExtra("event_booking_id",it.data.event_booking_id.toString())
                            startActivity(intent)
                        }
                    }
                    else{
                        Toaster.showToast(this,getString(R.string.data_empty),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
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
        btnTryAgain.setOnClickListener {
            if (this.isConnected){
                includeNoInternetEventBooking.visibility= View.GONE
                linearNoDataFoundEventBooking.visibility= View.GONE
                constraintEventBookingMain.visibility= View.VISIBLE
                eventBookingPackageViewModel.fetchEventsBookingPackagesDetails(passedPackageId)
            }
        }
        Log.e("check count",personsCounter.value.toString())
        tvChange.setOnClickListener {
            onBackPressed()
        }
        ivMinus.setOnClickListener {
            if(personsCounter.value==0){
                //personsCounter.postValue(personsCounter.value!!)
            }
            else if(personsCounter.value!!>0){
                personsCounter.postValue(personsCounter.value!!-1)
            }
        }
        ivPlus.setOnClickListener {
            when {
                personsCounter.value!! <100 -> {
                    personsCounter.postValue(personsCounter.value!!+1)
                }
                personsCounter.value!! == 100 -> {
                    Toast.makeText(this,getString(R.string.max_count_reached),Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this,getString(R.string.max_count_reached),Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnProceedToPay.setOnClickListener {
            when {
                etEventBookingUserName.text.trim().toString().isBlank() -> {
                    Toaster.showToast(this,getString(R.string.name_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                etEventBookingUserEmail.text.trim().toString().isBlank() -> {
                    Toaster.showToast(this,getString(R.string.email_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                etEventBookingUserPhone.text.trim().toString().isBlank() -> {
                    Toaster.showToast(this,getString(R.string.phone_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                personsCounter.value!! <=0 -> {
                    Toaster.showToast(this,getString(R.string.increment_counter),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                else -> {
                    Log.e("totalamount",totalAmount.value.toString())
                    Log.e("person count",personsCounter.value.toString())
                    eventBookingPackageViewModel.fetchEventsBookPackagesDetails(
                        passedPackageId,etEventBookingUserName.text.trim().toString(),
                        etEventBookingUserEmail.text.trim().toString(),
                        etEventBookingUserPhone.text.trim().toString(),
                        totalAmount.value.toString(),personsCounter.value.toString()
                    )
                }
            }
        }
    }

    override fun getViewBinging(): ActivityEventBookingBinding {
        return ActivityEventBookingBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
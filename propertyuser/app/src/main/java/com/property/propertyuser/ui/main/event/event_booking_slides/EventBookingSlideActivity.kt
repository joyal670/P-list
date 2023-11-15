package com.property.propertyuser.ui.main.event.event_booking_slides

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityEventBookingSlideBinding
import com.property.propertyuser.databinding.ActivitySplashBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.event_package.EventPackage
import com.property.propertyuser.ui.main.event.event_booking_slides.adapter.EventBookingAdapter
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_event_booking_slide.*
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.layout_no_network.*


class EventBookingSlideActivity : BaseActivity<ActivityEventBookingSlideBinding>(),ActivityListener {
    private lateinit var eventBookingSlideViewModel: EventBookingSlideViewModel
    private var eventPackageList= ArrayList<EventPackage>()
    private var passedEventId=""
    override fun getViewBinging(): ActivityEventBookingSlideBinding = ActivityEventBookingSlideBinding.inflate(layoutInflater)
    private val eventImages = ArrayList<Int>()
    override val layoutId: Int
        get() = R.layout.activity_event_booking_slide
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle("Event Booking")
        eventPackageList= ArrayList()
        passedEventId=intent.getStringExtra("event_id").toString()
        eventBookingSlideViewModel.fetchEventsPackages(passedEventId)

    }
    private fun setEventSlider(){

        viewpagerEventBooking.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        viewpagerEventBooking.adapter=EventBookingAdapter(this,eventPackageList)
        viewpagerEventBooking.offscreenPageLimit=3
        val pageMargin =
            resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset =
            resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        viewpagerEventBooking.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if(viewpagerEventBooking.orientation==ViewPager2.ORIENTATION_HORIZONTAL){
                if(ViewCompat.getLayoutDirection(viewpagerEventBooking)==ViewCompat.LAYOUT_DIRECTION_RTL){
                    page.translationX=-myOffset
                }else{
                    page.translationX=myOffset
                }
            }
            else{
                page.translationY=myOffset
            }
        }
        indicatorEventBooking.setViewPager2(viewpagerEventBooking)
    }


    override fun setupUI() {

    }

    override fun setupViewModel() {
        eventBookingSlideViewModel= EventBookingSlideViewModel()
    }

    override fun setupObserver() {
        eventBookingSlideViewModel.getEventPackageResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        if(it.data.event_packages_data!=null){
                            if(!(it.data.event_packages_data.event_packages.isNullOrEmpty())){
                                includeNoInternetEvent.visibility= View.GONE
                                linearNoDataFoundEvent.visibility= View.GONE
                                linearMainEventBooking.visibility= View.VISIBLE
                                eventPackageList=it.data.event_packages_data.event_packages as ArrayList<EventPackage>
                                setEventSlider()
                            }
                            else{
                                includeNoInternetEvent.visibility= View.GONE
                                linearNoDataFoundEvent.visibility= View.VISIBLE
                                linearMainEventBooking.visibility= View.GONE
                            }
                        }
                    }
                    else{
                        includeNoInternetEvent.visibility= View.GONE
                        linearNoDataFoundEvent.visibility= View.VISIBLE
                        linearMainEventBooking.visibility= View.GONE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    includeNoInternetEvent.visibility= View.GONE
                    linearNoDataFoundEvent.visibility= View.VISIBLE
                    linearMainEventBooking.visibility= View.GONE
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetEvent.visibility= View.VISIBLE
                        linearNoDataFoundEvent.visibility= View.GONE
                        linearMainEventBooking.visibility= View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternetEvent.visibility= View.GONE
                linearNoDataFoundEvent.visibility= View.GONE
                linearMainEventBooking.visibility= View.VISIBLE
                eventBookingSlideViewModel.fetchEventsPackages(passedEventId)
            }
        }
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
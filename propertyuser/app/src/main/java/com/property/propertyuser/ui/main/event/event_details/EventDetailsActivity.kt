package com.property.propertyuser.ui.main.event.event_details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.appbar.AppBarLayout
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityEventDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.event.event_booking_slides.EventBookingSlideActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.activity_event_details.btnBookNow
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlin.math.abs


class EventDetailsActivity : BaseActivity<ActivityEventDetailsBinding>(), ActivityListener {
    private lateinit var eventDetailsViewModel: EventDetailsViewModel
    private var passedEventId=""
    private var eventName=""
    private var lat=""
    private var lng=""
    private var eventAdminPhone=""
    override fun getViewBinging(): ActivityEventDetailsBinding = ActivityEventDetailsBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_event_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarCollapsingEvent)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbarCollapsingEvent.title=""


        app_bar_event.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 ->{
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                    toolbarCollapsingEvent.title=""

                }
                //  State Collapsed
                abs(verticalOffset) >= appBarLayout.totalScrollRange ->{
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                    toolbarCollapsingEvent.title=eventName

                }
                else -> Log.e("onelse","toolbar")//  Do anything for Ideal State

            }
        })
        passedEventId=intent.getIntExtra("event_id",-1).toString()
        if(passedEventId!=null){
            eventDetailsViewModel.fetchEventsDetails(passedEventId)
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        eventDetailsViewModel= EventDetailsViewModel()
    }

    override fun setupObserver() {
        eventDetailsViewModel.getEventDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data?.event_data!=null){
                        setSupportActionBar(toolbarCollapsingEvent)
                        supportActionBar!!.setDisplayShowTitleEnabled(false)
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        coordinatorEventDetails.visibility=View.VISIBLE
                        linearNodataNoInternet.visibility=View.GONE
                        includeNoInternet.visibility=View.GONE
                        linearNoDataFound.visibility=View.GONE
                        app_bar_event.visibility=View.VISIBLE
                        eventScrollingLayout.visibility=View.VISIBLE
                        linearBottom.visibility=View.VISIBLE
                        eventName=it.data.event_data.name
                        toolbarCollapsingEvent.title=it.data.event_data.name
                        binding.eventScrollingLayout.tvEventName.text=it.data.event_data.name

                        binding.eventScrollingLayout.tvEventDescription1.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(it.data.event_data.short_description, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(it.data.event_data.short_description)
                        }
                        binding.eventScrollingLayout.tvEventDescription2.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(it.data.event_data.description, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(it.data.event_data.description)
                        }

                        binding.eventScrollingLayout.tvEventDate.text=this.getDateStringToAnotherFormat(it.data.event_data.event_date)
                        if(it.data.event_data.event_docs!=null){
                            binding.ivEventDetailsImage.loadImagesWithGlideExt(it.data.event_data.event_docs[0].image)
                        }
                        lat=it.data.event_data.latitude
                        lng=it.data.event_data.longitude
                        if(it.data.event_data.admin_events!=null){
                            eventAdminPhone=it.data.event_data.admin_events.phone
                        }
                    }
                    else{
                        setSupportActionBar(toolbar)
                        supportActionBar!!.setDisplayShowTitleEnabled(false)
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        coordinatorEventDetails.visibility=View.VISIBLE
                        linearNodataNoInternet.visibility=View.VISIBLE
                        includeNoInternet.visibility=View.GONE
                        linearNoDataFound.visibility=View.VISIBLE
                        app_bar_event.visibility=View.GONE
                        eventScrollingLayout.visibility=View.GONE
                        linearBottom.visibility=View.GONE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    setSupportActionBar(toolbar)
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    coordinatorEventDetails.visibility=View.VISIBLE
                    linearNodataNoInternet.visibility=View.VISIBLE
                    includeNoInternet.visibility=View.GONE
                    linearNoDataFound.visibility=View.VISIBLE
                    app_bar_event.visibility=View.GONE
                    eventScrollingLayout.visibility=View.GONE
                    linearBottom.visibility=View.GONE
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        setSupportActionBar(toolbar)
                        supportActionBar!!.setDisplayShowTitleEnabled(false)
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        coordinatorEventDetails.visibility=View.VISIBLE
                        linearNodataNoInternet.visibility= View.VISIBLE
                        includeNoInternet.visibility=View.VISIBLE
                        linearNoDataFound.visibility=View.GONE
                        app_bar_event.visibility=View.GONE
                        eventScrollingLayout.visibility=View.GONE
                        linearBottom.visibility=View.GONE
                    }
                }

            }
        })
    }
    private fun getPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callPhone()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val intent =
            Intent(Intent.ACTION_CALL, Uri.parse("tel:" + eventAdminPhone))
        startActivity(intent)

    }
    private fun callPhone(){
        permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
            if(result.allGranted()){
                getPermission()
            }
            if(result.allShouldShowRationale()){
                Toaster.showToast(this,"Please allow permissions", Toaster.State.WARNING,Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                coordinatorEventDetails.visibility=View.VISIBLE
                linearNodataNoInternet.visibility=View.GONE
                if(passedEventId!=null){
                    eventDetailsViewModel.fetchEventsDetails(passedEventId)
                }
            }
        }
        btnBookNow.setOnClickListener {
            val intent=Intent(this,EventBookingSlideActivity::class.java)
            intent.putExtra("event_id",passedEventId)
            startActivity(intent)
        }
        binding.eventScrollingLayout.tvEventLocation.setOnClickListener {
            // Creates an Intent that will load a map of place
            val gmmIntentUri = Uri.parse("geo:${lat},${lng} ? q=${lat},${lng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }
        binding.btnCall.setOnClickListener {
            getPermission()
        }
        binding.btnMessage.setOnClickListener {
            val message = "Hallo"
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(
                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", eventAdminPhone, message))))
        }

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {

    }


}
package com.property.propertyuser.ui.main.maintenance.status_details

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityStatusDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.maintenance.status_details.adapter.StatusDetailedTimeLineAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_status_details.*
import kotlinx.android.synthetic.main.layout_no_network.*

class StatusDetailsActivity : BaseActivity<ActivityStatusDetailsBinding>(), ActivityListener {
    private lateinit var statusDetailsViewModel: StatusDetailsViewModel
    private var userServiceId=""
    private var requestedID=""
    override fun getViewBinging(): ActivityStatusDetailsBinding = ActivityStatusDetailsBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_status_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun onResume() {
        super.onResume()
        if(AppPreferences.reload_service_status_list_for_payment){
            AppPreferences.reload_service_status_list_for_payment=false
            statusDetailsViewModel.fetchSingleRequestedServiceDetails(userServiceId)
        }
    }
    override fun initData() {
        setTitle(resources.getString(R.string.status))
        userServiceId=intent.getStringExtra("user_service_id").toString()
        Log.e("serviceId",userServiceId)
        statusDetailsViewModel.fetchSingleRequestedServiceDetails(userServiceId)
    }
    private fun setStatusDetailedRecyclerView(status: Int, id: Int, estimateFile: String) {
        rvStatusDetailList.layoutManager = LinearLayoutManager(this)
        rvStatusDetailList.adapter = StatusDetailedTimeLineAdapter(this,status,id, estimateFile)
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        statusDetailsViewModel= StatusDetailsViewModel()
    }

    override fun setupObserver() {
        statusDetailsViewModel.getSingleRequestedServiceDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    includeNoInternet.visibility=View.GONE
                    if(it.data?.user_service!=null){
                        linearNoDataFoundStatusDetails.visibility=View.GONE
                        nestedStatusDetails.visibility=View.VISIBLE
                        if(it.data.user_service.service_related!=null){
                            tvServiceName.text=
                                getString(R.string.tvServiceName_title)+it.data.user_service.service_related.service
                        }
                        tvTimeStatus.text=it.data.user_service.time
                        tvDateStatus.text=it.data.user_service.date
                        tvPropertyName.text=getString(R.string.tvPropertyName_new)+it.data.user_service.property_name
                        setStatusDetailedRecyclerView(it.data.user_service.status,it.data.user_service.id, it.data.user_service.estimate_file)
                        requestedID=it.data.user_service.id.toString()
                        if(it.data.user_service.status==3){
                            btnCancelRequest.visibility= View.GONE
                        }
                        else{
                            btnCancelRequest.visibility= View.VISIBLE
                        }
                    }
                    else{
                        linearNoDataFoundStatusDetails.visibility=View.VISIBLE
                        nestedStatusDetails.visibility=View.GONE
                    }
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
                        includeNoInternet.visibility=View.VISIBLE
                        nestedStatusDetails.visibility=View.GONE
                    }
                }

            }
        })
        statusDetailsViewModel.getCancelRequestedServiceServiceDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(this,it.data!!.response,
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                    AppPreferences.reload_service_status_list_for_cancel_service=true
                    finish()
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
        btnCancelRequest.setOnClickListener {
            statusDetailsViewModel.cancelRequestedServiceDetails(requestedID)
        }
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternet.visibility=View.GONE
                nestedStatusDetails.visibility=View.VISIBLE
                statusDetailsViewModel.fetchSingleRequestedServiceDetails(userServiceId)
            }
        }
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.activity

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.adapter.StatusDetailedTimeLineAdapter
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.viewmodel.StatusDetailsViewModel
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected

import kotlinx.android.synthetic.main.activity_status_detailed.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class StatusDetailedActivity : BaseActivity() {
    private lateinit var statusDetailsViewModel : StatusDetailsViewModel
    private var passedId = ""
    private var saveStatus : Int = -1

    override val layoutId : Int
        get() = R.layout.activity_status_detailed
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.Status)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        passedId = intent.getStringExtra("request_id").toString()
        statusDetailsViewModel.ownerRequestedServiceDetails(passedId)

    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.reload_service_status_list_for_payment) {
            AppPreferences.reload_service_status_list_for_payment = false
            statusDetailsViewModel.ownerRequestedServiceDetails(passedId)
        }
    }

    private fun setStatusDetailTimelineRecyclerView(
        status : Int ,
        passedId : Int ,
        estimateFile : String
    ) {
        rvStatusDetailList.layoutManager = LinearLayoutManager(this)
        rvStatusDetailList.adapter = StatusDetailedTimeLineAdapter(this , status , passedId, estimateFile)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        statusDetailsViewModel = StatusDetailsViewModel()
    }

    override fun setupObserver() {
        statusDetailsViewModel.getOwnerRequestedServiceDetailsResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data != null) {
                        Log.e("response" , Gson().toJson(it))
                        if (it.data.data != null) {
                            if (it.data.data.owner_service != null) {
                                tvOrderIdDetails.text = it.data.data.owner_service.id.toString()
                                if (it.data.data.owner_service.service_related != null) {
                                    tvServiceNameDetails.text =
                                        it.data.data.owner_service.service_related.service
                                }
                                tvTimeDetail.text = it.data.data.owner_service.time
                                tvDateDetail.text = it.data.data.owner_service.date
                                tvPropertyNameDetail.text = it.data.data.owner_service.property_name
                                if (saveStatus == -1) {
                                    saveStatus = it.data.data.owner_service.status
                                } else {
                                    AppPreferences.reload_service_status_list_for_cancel_service =
                                        true
                                }
                                setStatusDetailTimelineRecyclerView(it.data.data.owner_service.status , it.data.data.owner_service.id, it.data.data.owner_service.estimate_file)

                                if(it.data.data.owner_service.status == 3)
                                {
                                    btnCancelRequest.visibility = View.GONE
                                }else{
                                    btnCancelRequest.visibility = View.VISIBLE
                                }

                            }
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
                    showProgressOwner()
                }
            }
        })
        statusDetailsViewModel.getCancelOwnerServiceResponse()
            .observe(this , androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showProgressOwner()
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        Toaster.showToast(this , it.data!!.response ,
                            Toaster.State.SUCCESS , Toast.LENGTH_LONG)
                      /*  AppPreferences.reload_service_status_list_for_cancel_service = true
                        onBackPressed()*/
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(this , getString(R.string.internal_server_error) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG)
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(this ,
                            it.data!!.response ,
                            Toaster.State.ERROR ,
                            Toast.LENGTH_LONG)
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

                }
            })
    }

    override fun onClicks() {
        btnCancelRequest.setOnClickListener {
            statusDetailsViewModel.cancelOwnerServiceDetails(passedId)
        }
    }

}
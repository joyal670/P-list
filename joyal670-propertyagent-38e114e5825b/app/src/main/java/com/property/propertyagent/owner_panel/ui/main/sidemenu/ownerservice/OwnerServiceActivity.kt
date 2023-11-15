package com.property.propertyagent.owner_panel.ui.main.sidemenu.ownerservice

import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_request_service_for_approval_list.*
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_owner_service.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class OwnerServiceActivity : BaseActivity() {
    private lateinit var ownerServiceViewModel : OwnerServiceViewModel
    private var passedPosition = -1
    private var requestDetailsList = ArrayList<RequestDetail>()
    private lateinit var ownerServiceAdapter : OwnerServiceAdapter
    private var isLoading : Boolean = false
    private var page : Int = 1
    private var totalPages : Int = 0
    private var selectedType = 0

    override val layoutId : Int
        get() = R.layout.activity_owner_service
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.service_request)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout.setRefreshing(false)

        ownerServiceViewModel.ownerRequestedServiceForApprovalList(page.toString())
    }

    override fun setupUI() {

    }

    private fun setOwnerServiceRecyclerView() {
        activity_owner_service_recyclerview.layoutManager = LinearLayoutManager(this)
        ownerServiceAdapter = OwnerServiceAdapter(requestDetailsList ,
            { it , pos -> acceptService(it , pos) } ,
            { it , pos -> rejectService(it , pos) })
        activity_owner_service_recyclerview.adapter = ownerServiceAdapter
        activity_owner_service_recyclerview.scheduleLayoutAnimation()

        activity_owner_service_recyclerview.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            requestDetailsList.add(RequestDetail("" ,
                "" ,
                -1 ,
                PropertyDetails(-1 ,
                    "" ,
                    CityRel(-1 , "") ,
                    "" ,
                    CountryRel(-1 , "") ,
                    -1 ,
                    -1 ,
                    "" ,
                    PropertyPriorityImage("" , -1) ,
                    "" ,
                    -1 ,
                    "" ,
                    StateRel(-1 , "") ,
                    "" ,
                    "" ,
                    -1 ,
                    ZipcodeRel(-1 , ""),"", "") ,
                -1 ,
                ServiceRelated(-1 , "" , "") ,
                -1 ,
                "" ,
                -1 ,
                -1 ,
                UserPropertyRelated(-1 , -1) ,
                UserServiceRelated(-1 , -1 , -1)))
            ownerServiceAdapter.notifyItemInserted(requestDetailsList.size - 1)
            ownerServiceViewModel.ownerRequestedServiceForApprovalList(page.toString())
        }
    }

    private fun rejectService(passedId : String , pos : Int) {
        passedPosition = pos
        ownerServiceViewModel.ownerRejectRequestedService(passedId)
    }

    private fun acceptService(passedId : String , pos : Int) {
        passedPosition = pos
        ownerServiceViewModel.ownerAcceptRequestedServiceDetails(passedId)
    }

    override fun setupViewModel() {
        ownerServiceViewModel = OwnerServiceViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        ownerServiceViewModel.getOwnerRequestedServiceForApprovalListResponse()
            .observe(this , androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showProgressOwner()
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        if (it.data!!.data != null) {
                            totalPages = it.data.data.total_page_count
                            if (requestDetailsList.size != 0) {
                                isLoading = false
                                page += 1
                                requestDetailsList.removeAt(requestDetailsList.size - 1)
                                ownerServiceAdapter.notifyItemRemoved(requestDetailsList.size)
                                requestDetailsList.addAll(it.data.data.request_details)
                                ownerServiceAdapter.notifyDataSetChanged()
                            } else {
                                page += 1
                                requestDetailsList =
                                    it.data.data.request_details as ArrayList<RequestDetail>
                                setOwnerServiceRecyclerView()
                            }

                            if (requestDetailsList.size == 0) {
                                llEmptyData.isVisible = true
                            }
                        }

                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(this , getString(R.string.internal_server_error) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG)
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(this ,
                            getString(R.string.data_empty) ,
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
        ownerServiceViewModel.getOwnerAcceptRequestedServiceResponse()
            .observe(this , androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showProgressOwner()
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        Log.e("response--details" , Gson().toJson(it))
                        if (it.data != null) {
                            Toaster.showToast(this , it.data.response ,
                                Toaster.State.SUCCESS , Toast.LENGTH_LONG)
                            requestDetailsList[passedPosition].status = 1
                            ownerServiceAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(this , getString(R.string.internal_server_error) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG)
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(this ,
                            getString(R.string.data_empty) ,
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
        ownerServiceViewModel.getOwnerRejectRequestedServiceResponse()
            .observe(this , androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showProgressOwner()
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        Log.e("response--details" , Gson().toJson(it))
                        if (it.data != null) {
                            Toaster.showToast(this , it.data.response ,
                                Toaster.State.SUCCESS , Toast.LENGTH_LONG)
                            requestDetailsList[passedPosition].status = 2
                            ownerServiceAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(this , getString(R.string.internal_server_error) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG)
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(this ,
                            getString(R.string.data_empty) ,
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

        swipeRefreshLayout.setOnRefreshListener {
            refreshUi()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUi() {
        page = 1
        requestDetailsList.clear()
        ownerServiceAdapter.notifyDataSetChanged()
        initData()
    }

}
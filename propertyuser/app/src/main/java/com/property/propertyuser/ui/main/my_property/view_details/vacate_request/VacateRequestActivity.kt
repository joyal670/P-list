package com.property.propertyuser.ui.main.my_property.view_details.vacate_request

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityVacateRequestBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.vacate_request_list.UserPropertyRelated
import com.property.propertyuser.modal.vacate_request_list.UserVacateRequest
import com.property.propertyuser.ui.main.my_property.view_details.generate_ticket_vacate_request.GenerateTicketVacateRequestActivity
import com.property.propertyuser.ui.main.my_property.view_details.vacate_request.adapter.VacateRequestedTicketAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_vacate_request.*
import java.text.SimpleDateFormat
import java.util.*

class VacateRequestActivity : BaseActivity<ActivityVacateRequestBinding>(), ActivityListener {
    private var userPropertyId = ""
    private var checkIn = false
    private lateinit var vacateRequestViewModel: VacateRequestViewModel
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0
    private var userVacateRequests = ArrayList<UserVacateRequest>()
    private lateinit var vacateRequestedTicketAdapter: VacateRequestedTicketAdapter


    override fun getViewBinging(): ActivityVacateRequestBinding =
        ActivityVacateRequestBinding.inflate(layoutInflater)

    override val layoutId: Int
        get() = R.layout.activity_vacate_request
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(resources.getString(R.string.vacate_request))
        userPropertyId = intent.getStringExtra("user_property_id").toString()
        vacateRequestViewModel.vacateRequestList(page.toString(), userPropertyId)
    }

    private fun setupVacateRequestRecyclerView() {
        rvVacateRequestList.layoutManager = LinearLayoutManager(this)
        vacateRequestedTicketAdapter =
            VacateRequestedTicketAdapter(this, userVacateRequests) { vacateRequestViewDetails(it) }
        rvVacateRequestList.adapter = vacateRequestedTicketAdapter

        Log.e("TAG", "setupVacateRequestRecyclerView: ${userVacateRequests.size}" )
        if(userVacateRequests.size == 0){
            binding.noData.visibility = View.VISIBLE
            binding.rvVacateRequestList.visibility = View.GONE
        }else {
            binding.noData.visibility = View.GONE
            binding.rvVacateRequestList.visibility = View.VISIBLE
        }


        rvVacateRequestList.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            userVacateRequests.add(UserVacateRequest(-1, UserPropertyRelated(-1, ""), -1, ""))

            vacateRequestedTicketAdapter.notifyItemInserted(userVacateRequests.size - 1)
            vacateRequestViewModel.vacateRequestList(page.toString(), userPropertyId)
        }
    }

    private fun vacateRequestViewDetails(it: Int) {
        val intent = Intent(this, GenerateTicketVacateRequestActivity::class.java)
        intent.putExtra("vacate_id", it.toString())
        startActivity(intent)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        vacateRequestViewModel = VacateRequestViewModel()
    }

    override fun setupObserver() {
        vacateRequestViewModel.getVacateRequestListResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("responseproceeddetails", Gson().toJson(it))
                        if (it.data?.user_vacate_data != null) {
                            totalPages = it.data.user_vacate_data.total_page_count
                            if (it.data.user_vacate_data.user_vacate_requests.isNotEmpty()) {
                                if (userVacateRequests.size != 0) {
                                    isLoading = false
                                    page += 1
                                    userVacateRequests.removeAt(userVacateRequests.size - 1)
                                    vacateRequestedTicketAdapter.notifyItemRemoved(
                                        userVacateRequests.size
                                    )
                                    userVacateRequests.addAll(it.data.user_vacate_data.user_vacate_requests)
                                    vacateRequestedTicketAdapter.notifyDataSetChanged()
                                } else {
                                    page += 1
                                    userVacateRequests =
                                        it.data.user_vacate_data.user_vacate_requests as ArrayList<UserVacateRequest>

                                }
                            }
                        }
                        setupVacateRequestRecyclerView()
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }

                }
            })
        vacateRequestViewModel.getVacateRequestResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("responseproceeddetails", Gson().toJson(it))
                        Toaster.showToast(
                            this, it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        //vacateRequestViewModel.vacateRequestList("1", userPropertyId)
                        initData()

                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }

                }
            })
    }

    override fun onClicks() {
        btnSendRequest.setOnClickListener {
            if (!checkIn) {
                Toaster.showToast(
                    this, getString(R.string.date_required),
                    Toaster.State.WARNING, Toast.LENGTH_SHORT
                )
            } else {
                vacateRequestViewModel.vacateRequest(
                    userPropertyId,
                    tvSelectVacateDate.text.trim().toString()
                )
            }
        }
        tvSelectVacateDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val picker = builder.build()
            this.supportFragmentManager.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy") // modify format
                    val date = formatter.format(Date(it))
                    tvSelectVacateDate.text = date.toString()
                    tvSelectVacateDate.setTextColor(Color.BLACK)
                    checkIn = true
                }
            }
        }

        btnDiscard.setOnClickListener {
            super.onBackPressed()
        }

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
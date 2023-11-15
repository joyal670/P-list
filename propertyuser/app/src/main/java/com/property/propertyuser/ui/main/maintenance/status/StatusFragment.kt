package com.property.propertyuser.ui.main.maintenance.status

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentStatusBinding
import com.property.propertyuser.modal.list_requested_service_details.ServiceAmount
import com.property.propertyuser.modal.list_requested_service_details.ServiceRelated
import com.property.propertyuser.modal.list_requested_service_details.UserService
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.maintenance.status.adapter.StatusAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_status.*
import kotlinx.android.synthetic.main.layout_no_network.*

class StatusFragment : BaseFragment() {
    private var userPropertyId = ""
    private var isLoading: Boolean = false
    private var pageNo = 0
    private var totalPageCount = 0
    private var userServicesList = ArrayList<UserService>()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var statusAdapter: StatusAdapter

    companion object {
        const val ARG_PROPERTY_ID = "user_property_id"
        fun newInstance(user_property_id: String): StatusFragment {
            val fragment = StatusFragment()

            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, user_property_id)
                Log.e("ARG_PROPERTY_ID", user_property_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var statusViewModel: StatusViewModel
    private lateinit var binding: FragmentStatusBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPropertyId = arguments?.getString(ARG_PROPERTY_ID).toString()
        binding = FragmentStatusBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_status,container,false)*/
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.reload_service_status_list_for_cancel_service) {
            pageNo = 1
            userServicesList = ArrayList()
            AppPreferences.reload_service_status_list_for_cancel_service = false
            statusViewModel.fetchRequestedServiceListDetails(userPropertyId, pageNo.toString())
        }

    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        pageNo = 1
        userServicesList = ArrayList()
        statusViewModel.fetchRequestedServiceListDetails(userPropertyId, pageNo.toString())
    }

    private fun setStatusRecyclerView() {

        if (userServicesList.size == 0) {
            includeNoInternet.visibility = View.GONE
            linearNoDataFoundStatus.visibility = View.VISIBLE
            rvStatusList.visibility = View.GONE
        } else {
            includeNoInternet.visibility = View.GONE
            linearNoDataFoundStatus.visibility = View.GONE
            rvStatusList.visibility = View.VISIBLE
        }


        layoutManager = LinearLayoutManager(requireContext())
        rvStatusList.layoutManager = layoutManager
        statusAdapter = StatusAdapter(requireContext(), userServicesList, userPropertyId)
        rvStatusList.adapter = statusAdapter

        rvStatusList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (pageNo <= totalPageCount) {
            userServicesList.add(
                UserService(
                    "", -1, -1, ServiceRelated(-1, ""), -1, "", "",
                    ServiceAmount(-1, "")
                )
            )
            statusAdapter.notifyItemInserted(userServicesList.size - 1)
            statusViewModel.fetchRequestedServiceListDetails(userPropertyId, pageNo.toString())
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        statusViewModel = StatusViewModel()
    }

    override fun setupObserver() {
        statusViewModel.getRequestedServiceListResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        if (userServicesList.size == 0) {
                            showLoader()
                        }
                    }
                    Status.SUCCESS -> {
                        if (userServicesList.size == 0) {
                            dismissLoader()
                        }
                        Log.e("responseproceeddetails", Gson().toJson(it))
                        if (it.data!!.user_service_data != null) {
                            totalPageCount = it.data.user_service_data.total_page_count
                            if (userServicesList.size != 0) {
                                isLoading = false
                                pageNo += 1
                                userServicesList.removeAt(userServicesList.size - 1)
                                statusAdapter.notifyItemRemoved(userServicesList.size)
                                if (!(it.data.user_service_data.user_services.isNullOrEmpty())) {
                                    userServicesList.addAll(it.data.user_service_data.user_services)
                                    statusAdapter.notifyDataSetChanged()
                                }
                            } else {
                                pageNo += 1
                                if (it.data.user_service_data.user_services != null) {
                                    userServicesList =
                                        it.data.user_service_data.user_services as ArrayList<UserService>
                                    setStatusRecyclerView()
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            includeNoInternet.visibility = View.VISIBLE
                            rvStatusList.visibility = View.GONE
                            linearNoDataFoundStatus.visibility = View.GONE
                        }
                    }

                }
            })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                rvStatusList.visibility = View.VISIBLE
                pageNo = 1
                statusViewModel.fetchRequestedServiceListDetails(userPropertyId, pageNo.toString())
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            userServicesList.clear()
            statusAdapter.notifyDataSetChanged()
            initData()
        }
    }
}
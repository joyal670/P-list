package com.property.propertyuser.ui.main.maintenance.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentServiceBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.service_list.Service
import com.property.propertyuser.ui.main.maintenance.MaintenanceActivity
import com.property.propertyuser.ui.main.maintenance.service.adapter.ServiceAdapter
import com.property.propertyuser.ui.main.my_property.view_details.service_details.ServiceDetailsActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEndGrid
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_service.*
import kotlinx.android.synthetic.main.layout_no_network.*

class ServiceFragment : BaseFragment() {
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentServiceBinding
    private var userPropertyId = ""
    private var propertyId = ""
    private var isLoading: Boolean = false
    private var i = 1
    private var totalPageCount = 0
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var serviceAdapter: ServiceAdapter
    private var servicesList = ArrayList<Service>()
    var searchTextLiveData: MutableLiveData<String>? = null

    init {
        searchTextLiveData = MutableLiveData()
    }

    companion object {
        const val ARG_PROPERTY_ID = "property_id"
        const val ARG_USER_PROPERTY_ID = "user_property_id"
        fun newInstance(property_id: String, user_property_id: String): ServiceFragment {
            val fragment = ServiceFragment()

            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, property_id)
                putString(ARG_USER_PROPERTY_ID, user_property_id)
                Log.e("ARG_USER_PROPERTY_ID", user_property_id)
                Log.e("ARG_PROPERTY_ID", property_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propertyId = arguments?.getString(ARG_PROPERTY_ID).toString()
        userPropertyId = arguments?.getString(ARG_USER_PROPERTY_ID).toString()
        binding = FragmentServiceBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_service,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as MaintenanceActivity

    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        servicesList = ArrayList()
        serviceViewModel.fetchServiceListDetails("", i.toString(), propertyId)
    }

    private fun setServiceRecyclerView() {
        layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvServiceList.layoutManager = layoutManager
        serviceAdapter = ServiceAdapter(requireContext(), servicesList) { i: Int, s: String ->
            selectedService(
                i,
                s
            )
        }
        rvServiceList.adapter = serviceAdapter
        rvServiceList.addOnScrolledToEndGrid {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (i <= totalPageCount) {
            servicesList.add(Service(-1, "", ""))
            serviceAdapter.notifyItemInserted(servicesList.size - 1)

            serviceViewModel.fetchServiceListDetails("", i.toString(), userPropertyId)
        }

    }

    private fun selectedService(serviceId: Int, serviceName: String) {
        Log.e("idname", serviceName)
        val intent = Intent(context, ServiceDetailsActivity::class.java)
        intent.putExtra("service_id", serviceId.toString())
        intent.putExtra("service_name", serviceName)
        intent.putExtra("user_property_id", userPropertyId)
        activity?.startActivityForResult(intent, 33)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.service_request))
    }

    override fun setupViewModel() {
        serviceViewModel = ServiceViewModel()
    }

    override fun setupObserver() {
        serviceViewModel.getServiceListResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (servicesList.size == 0) {
                        showLoader()
                    }
                }
                Status.SUCCESS -> {
                    if (servicesList.size == 0) {
                        dismissLoader()
                    }
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    if (it.data!!.service_data != null) {
                        totalPageCount = it.data.service_data.total_page_count
                        if (servicesList.size != 0) {
                            isLoading = false
                            i += 1
                            servicesList.removeAt(servicesList.size - 1)
                            serviceAdapter.notifyItemRemoved(servicesList.size)
                            if (!(it.data.service_data.services.isNullOrEmpty())) {
                                servicesList.addAll(it.data.service_data.services)
                                serviceAdapter.notifyDataSetChanged()
                            }
                        } else {
                            if (!(it.data.service_data.services.isNullOrEmpty())) {
                                includeNoInternet.visibility = View.GONE
                                serviceMainConstraint.visibility = View.VISIBLE
                                linearNoDataFoundService.visibility = View.GONE
                                i += 1
                                servicesList =
                                    it.data.service_data.services as ArrayList<Service>
                                setServiceRecyclerView()
                            } else {
                                includeNoInternet.visibility = View.GONE
                                serviceMainConstraint.visibility = View.GONE
                                linearNoDataFoundService.visibility = View.VISIBLE
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
                        serviceMainConstraint.visibility = View.GONE
                        linearNoDataFoundService.visibility = View.GONE
                    }
                }

            }
        })

        searchTextLiveData?.observe(this) { text ->
            serviceAdapter.filter.filter(text)
        }
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                serviceMainConstraint.visibility = View.VISIBLE
                i = 1
                serviceViewModel.fetchServiceListDetails("", i.toString(), propertyId)
            }
        }

        binding.etSearchHome.doOnTextChanged { text, start, before, count ->
            searchTextLiveData?.value = text.toString()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            i = 1
            servicesList.clear()
            serviceAdapter.notifyDataSetChanged()
            initData()
        }
    }

}
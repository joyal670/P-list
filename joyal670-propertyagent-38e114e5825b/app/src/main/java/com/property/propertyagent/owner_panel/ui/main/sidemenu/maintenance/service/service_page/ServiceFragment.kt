package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.databinding.FragmentServiceOwnerBinding
import com.property.propertyagent.dialogs.InternetDialogFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_maintance.Service
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_detailed_page.activity.ServiceDetailedActivity
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.viewmodel.MaintanceViewmodel
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected

class ServiceFragment : BaseFragment() {
    private lateinit var maintanaceViewmodel: MaintanceViewmodel
    private lateinit var binding: FragmentServiceOwnerBinding
    private var maintenanceList = ArrayList<Service>()
    private lateinit var serviceAdapter: ServiceAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentServiceOwnerBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        binding.swipeRefreshLayout.setRefreshing(false)

        maintanaceViewmodel.maintance(page.toString())
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter(maintenanceList) { it, name -> navigate(it, name) }
        binding.rvServiceList.adapter = serviceAdapter
        binding.rvServiceList.scheduleLayoutAnimation()
        binding.rvServiceList.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            maintenanceList.add(Service(-1, "", ""))
            serviceAdapter.notifyItemInserted(maintenanceList.size - 1)
            maintanaceViewmodel.maintance(page.toString())
        }
    }

    override fun setupUI() {
        /* set layout for recyclerview */
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.rvServiceList.layoutManager = gridLayoutManager

        /* search service */
        binding.etSearchService.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                serviceAdapter.filter.filter(newText.toString())
                return false
            }
        })
    }

    override fun setupViewModel() {
        maintanaceViewmodel = MaintanceViewmodel()
    }

    override fun setupObserver() {
        /* service api*/
        maintanaceViewmodel.maintanaceData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data?.services_data != null) {
                        if (!(it.data.services_data.services.isNullOrEmpty())) {
                            maintenanceList.addAll(it.data.services_data.services)
                            setupRecyclerView()
                        }
                    }
                    if (maintenanceList.size == 0) {
                        val dialog = InternetDialogFragment(2)
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
            }
        })
    }

    /* navigate to next page */
    /* pass id of selected service */
    private fun navigate(id: Int, name: String) {
        val intent = Intent(requireContext(), ServiceDetailedActivity::class.java)
        intent.putExtra(Constants.MAINTANCE_ID, id)
        intent.putExtra(Constants.MAINTANCE_NAME, name)
        activity?.startActivityForResult(intent, 33)
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshUi()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUi() {
        page = 1
        maintenanceList.clear()
        serviceAdapter.notifyDataSetChanged()
        initData()
    }


}
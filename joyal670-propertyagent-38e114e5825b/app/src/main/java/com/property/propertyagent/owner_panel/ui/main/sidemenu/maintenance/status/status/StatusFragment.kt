package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_list_requested_services.OwnerService
import com.property.propertyagent.modal.owner.owner_list_requested_services.ServiceRelated
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.activity.StatusDetailedActivity
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_status_owner.*

class StatusFragment : BaseFragment() {
    private lateinit var statusViewModel: StatusViewModel
    private var ownerServices = ArrayList<OwnerService>()
    private lateinit var statusAdapter: StatusAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_status_owner, container, false)
    }

    override fun initData() {

        swipeRefreshLayout.setRefreshing(false)

        statusViewModel.ownerRequestedServiceList(page.toString())
    }


    private fun setupStatusRecyclerView() {
        rvStatusList.layoutManager = LinearLayoutManager(context)
        statusAdapter = StatusAdapter(ownerServices) { startStatusDetail(it) }
        rvStatusList.adapter = statusAdapter
        rvStatusList.scheduleLayoutAnimation()
        rvStatusList.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            ownerServices.add(OwnerService("", -1, "", -1, ServiceRelated(-1, ""), -1, ""))
            statusAdapter.notifyItemInserted(ownerServices.size - 1)
            statusViewModel.ownerRequestedServiceList(page.toString())
        }
    }

    private fun startStatusDetail(id: String) {
        val intent = Intent(requireActivity(), StatusDetailedActivity::class.java)
        intent.putExtra("request_id", id.toString())
        requireActivity().startActivity(intent)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        statusViewModel = StatusViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        statusViewModel.getOwnerRequestedServiceListResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    if (it.data!!.owner_service_data != null) {
                        totalPages = it.data.owner_service_data.total_page_count
                        if (ownerServices.size != 0) {
                            isLoading = false
                            page += 1
                            ownerServices.removeAt(ownerServices.size - 1)
                            statusAdapter.notifyItemRemoved(ownerServices.size)
                            ownerServices.addAll(it.data.owner_service_data.owner_services)
                            statusAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            ownerServices =
                                it.data.owner_service_data.owner_services as ArrayList<OwnerService>
                            setupStatusRecyclerView()
                        }
                    }

                    if (ownerServices.size == 0) {
                        llEmptyData.isVisible = true
                        rvStatusList.isVisible = false
                    }
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
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.LOADING -> {
                    showProgressOwner()
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
        ownerServices.clear()
        statusAdapter.notifyDataSetChanged()
        initData()
    }

}
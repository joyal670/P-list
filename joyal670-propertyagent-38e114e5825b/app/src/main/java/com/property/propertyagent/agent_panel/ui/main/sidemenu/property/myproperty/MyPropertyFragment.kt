package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.myproperty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_assigned_property_list.*
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_myproperty.*

class MyPropertyFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var myPropertyViewModel: MyPropertyViewModel

    private var myPropertyList = ArrayList<UserProperty>()
    private lateinit var adapter: MyPropertyAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_myproperty, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_property))

        myPropertyViewModel.fetchAgentAssignedPropertyList(page.toString())
    }

    private fun setupMyPropertyRecyclerView() {

        mypropertyfrgRecycerview.layoutManager = LinearLayoutManager(context)
        adapter = MyPropertyAdapter(requireContext(), myPropertyList)
        mypropertyfrgRecycerview.adapter = adapter

        mypropertyfrgRecycerview.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            myPropertyList.add(
                UserProperty(
                    "", CityRel(-1, ""), "", CountryRel(-1, ""), -1,
                    "", "", "", "", PropertyPriorityImage("", -1), -1, "", StateRel(-1, "")
                )
            )
            adapter.notifyItemInserted(myPropertyList.size - 1)
            myPropertyViewModel.fetchAgentAssignedPropertyList(page.toString())
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        myPropertyViewModel = MyPropertyViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        myPropertyViewModel.getAgentAssignedPropertyListResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (myPropertyList.size == 0) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        if (myPropertyList.size == 0) {
                            dismissProgress()
                        }
                        if (it.data != null) {
                            if (!(it.data.response.user_properties.isNullOrEmpty())) {
                                totalPages = it.data.response.total_page_count
                                if (myPropertyList.size != 0) {
                                    isLoading = false
                                    page += 1
                                    myPropertyList.removeAt(myPropertyList.size - 1)
                                    adapter.notifyItemRemoved(myPropertyList.size)
                                    myPropertyList.addAll(it.data.response.user_properties)
                                    adapter.notifyDataSetChanged()
                                } else {
                                    if (it.data.response.user_properties.isNotEmpty()) {
                                        page += 1
                                        myPropertyList =
                                            it.data.response.user_properties as ArrayList<UserProperty>
                                        setupMyPropertyRecyclerView()
                                        mypropertyfrgRecycerview.isVisible = true
                                    } else {
                                        llEmptyData.isVisible = true
                                        mypropertyfrgRecycerview.isVisible = false
                                    }
                                }
                            } else {
                                llEmptyData.isVisible = true
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
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

    override fun onClicks() {

    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            myPropertyViewModel.fetchAgentAssignedPropertyList(page.toString())
        }
    }
}
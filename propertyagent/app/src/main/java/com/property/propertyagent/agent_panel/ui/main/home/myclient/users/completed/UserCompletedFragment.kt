package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.completed

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.UsersActivity
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.details.UsersPropertyViewDetailedActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.PropertyDetails
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.PropertyPriorityImage
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.UserProperty
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.UserRel
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.is_completed
import kotlinx.android.synthetic.main.fragment_completed.*

class UserCompletedFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var userCompletedViewModel: UserCompletedViewModel

    private var propertyList = ArrayList<UserProperty>()
    private lateinit var userCompletedAdapter: UserCompletedAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_completed, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as UsersActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.users))

        userCompletedViewModel.agentUserBookingPropertyCompletedList(page.toString())
    }

    private fun setCompletedRecyclerView() {
        completedfrgrecyclerView.layoutManager = LinearLayoutManager(context)
        userCompletedAdapter =
            UserCompletedAdapter(propertyList, { startComplete(it) },
                { openWhatsapp(it) }, { openDialer(it) })
        completedfrgrecyclerView.adapter = userCompletedAdapter

        completedfrgrecyclerView.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            propertyList.add(
                UserProperty(
                    "",
                    -1,
                    PropertyDetails(-1, "", ""),
                    -1,
                    PropertyPriorityImage("", -1),
                    "",
                    -1,
                    UserRel("", -1, "", "", "")
                )
            )
            userCompletedAdapter.notifyItemInserted(propertyList.size - 1)
            userCompletedViewModel.agentUserBookingPropertyCompletedList(page.toString())
        }
    }

    private fun startComplete(passedId: String) {
        is_completed = true
        val intent = Intent(requireActivity(), UsersPropertyViewDetailedActivity::class.java)
        intent.putExtra("tour_id", passedId)
        requireActivity().startActivity(intent)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        userCompletedViewModel = UserCompletedViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        userCompletedViewModel.getAgentUserBookingPropertyCompletedResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (page == 1) {
                            dismissProgress()
                        }
                        if (it.data?.tour_data != null) {
                            Log.e("response", Gson().toJson(it))
                            if (!(it.data.tour_data.user_properties.isNullOrEmpty())) {
                                totalPages = it.data.tour_data.total_page_count
                                if (propertyList.size != 0) {
                                    isLoading = false
                                    page += 1
                                    propertyList.removeAt(propertyList.size - 1)
                                    userCompletedAdapter.notifyItemRemoved(propertyList.size)
                                    propertyList.addAll(it.data.tour_data.user_properties)
                                    userCompletedAdapter.notifyDataSetChanged()
                                } else {
                                    if (it.data.tour_data.user_properties.isNotEmpty()) {
                                        page += 1
                                        propertyList =
                                            it.data.tour_data.user_properties as ArrayList<UserProperty>
                                        setCompletedRecyclerView()
                                    } else {
                                        llEmptyData.isVisible = true
                                        completedfrgrecyclerView.isVisible = false
                                    }
                                }
                            } else {
                                llEmptyData.isVisible = true
                                completedfrgrecyclerView.isVisible = false
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            it.message!!,
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
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        if (page == 1) {
                            showProgress()
                        }
                    }
                }
            }
    }

    override fun onClicks() {

    }

    private fun openDialer(phone: String) {
        permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
            if (result.allGranted()) {
                context?.let { CommonUtils.dailPhone(it, phone) }
            }
        }
    }

    private fun openWhatsapp(phone: String) {
        context?.let { CommonUtils.openWhatsAppEnquiry(it, phone, getString(R.string.hai)) }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            userCompletedViewModel.agentUserBookingPropertyCompletedList(page.toString())
        }
    }
}
package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.ongoing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.PropertyDetails
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.PropertyPriorityImage
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.UserProperty
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.UserRel
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_ongoing.*

class UserOngoingFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var userOngoingViewModel: UserOngoingViewModel

    private var propertyList = ArrayList<UserProperty>()
    private lateinit var userOnGoingAdapter: UserOnGoingAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_ongoing, container, false)
    }

    override fun initData() {

        fragmentTransInterface = activity as UsersActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.users))

        userOngoingViewModel.agentUserBookingPropertyOngoingList(page.toString())
    }

    private fun openDialer(phone: String) {
        permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
            if (result.allGranted()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }
        }
    }

    private fun openWhatsapp(phone: String) {
        val message = "Hallo"
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        phone, message
                    )
                )
            )
        )
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        userOngoingViewModel = UserOngoingViewModel()
    }

    private fun setOngoingRecyclerView() {
        ongoingfrgRecyclerView.layoutManager = LinearLayoutManager(context)
        userOnGoingAdapter =
            UserOnGoingAdapter(propertyList, { startOnGoingDetails(it) },
                { openWhatsapp(it) }, { openDialer(it) })

        ongoingfrgRecyclerView.adapter = userOnGoingAdapter

        ongoingfrgRecyclerView.addOnScrolledToEnd {
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
                    -1,
                    PropertyDetails(-1, "", ""),
                    -1,
                    PropertyPriorityImage("", -1),
                    -1,
                    UserRel("", -1, "", "", "")
                )
            )
            userOnGoingAdapter.notifyItemInserted(propertyList.size - 1)
            userOngoingViewModel.agentUserBookingPropertyOngoingList(page.toString())
        }
    }

    private fun startOnGoingDetails(passedId: String) {
        val intent = Intent(requireActivity(), UsersPropertyViewDetailedActivity::class.java)
        intent.putExtra("tour_id", passedId)
        requireActivity().startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        userOngoingViewModel.getAgentUserBookingPropertyOngoingResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (propertyList.size == 0) {
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
                                userOnGoingAdapter.notifyItemRemoved(propertyList.size)
                                propertyList.addAll(it.data.tour_data.user_properties)
                                userOnGoingAdapter.notifyDataSetChanged()
                            } else {
                                if (it.data.tour_data.user_properties.isNotEmpty()) {
                                    page += 1
                                    propertyList =
                                        it.data.tour_data.user_properties as ArrayList<UserProperty>
                                    setOngoingRecyclerView()
                                    ongoingfrgRecyclerView.isVisible = true
                                } else {
                                    llEmptyData.isVisible = true
                                    ongoingfrgRecyclerView.isVisible = false
                                }
                            }
                        } else {
                            llEmptyData.isVisible = true
                            ongoingfrgRecyclerView.isVisible = false
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
                    if (propertyList.size == 0) {
                        showProgress()
                    }
                }
            }
        }
    }

    override fun onClicks() {

    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            userOngoingViewModel.agentUserBookingPropertyOngoingList(page.toString())
        }
    }
}
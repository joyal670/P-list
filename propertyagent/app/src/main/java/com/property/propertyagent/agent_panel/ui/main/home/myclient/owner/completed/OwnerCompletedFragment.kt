package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.completed

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
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.OwnerActivity
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details.OwnerPropertyViewDetailedActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_owner_completed_list.OwnerRel
import com.property.propertyagent.modal.agent.agent_owner_completed_list.PropertyPriorityImage
import com.property.propertyagent.modal.agent.agent_owner_completed_list.UserProperty
import com.property.propertyagent.modal.agent.agent_owner_completed_list.UserPropertyRelated
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.is_completed
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_ownercompleted.*

class OwnerCompletedFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerCompletedViewModel: OwnerCompletedViewModel

    private var userPropertiesListCompleted = ArrayList<UserProperty>()
    private lateinit var ownerCompletedAdapter: OwnerCompletedAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_ownercompleted, container, false)
    }

    override fun initData() {

        fragmentTransInterface = activity as OwnerActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.owner))
        userPropertiesListCompleted = ArrayList()
        ownerCompletedViewModel.agentOwnerCompletedPropertiesListDetails(page.toString(), "1")
    }

    private fun setupOwnerCompletedRecyclerView() {
        OwnerCompletedfrgRecyclerView.layoutManager = LinearLayoutManager(context)
        ownerCompletedAdapter =
            OwnerCompletedAdapter(userPropertiesListCompleted,
                { tour_id -> openDetailsActivity(tour_id) },
                { phone -> openWhatsapp(phone) },
                { phone -> openDialer(phone) })

        OwnerCompletedfrgRecyclerView.adapter = ownerCompletedAdapter

        OwnerCompletedfrgRecyclerView.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    private fun openDetailsActivity(tourId: String) {
        is_completed = true
        val intent = Intent(requireContext(), OwnerPropertyViewDetailedActivity::class.java)
        intent.putExtra("tour_id", tourId)
        intent.putExtra("type", "completed")
        requireActivity().startActivity(intent)
    }

    fun loadData() {
        if (page <= totalPages) {
            userPropertiesListCompleted.add(
                UserProperty(
                    -1,
                    OwnerRel("", -1, "", "", ""),
                    -1,
                    PropertyPriorityImage("", -1),
                    UserPropertyRelated(-1, -1, "", "")
                )
            )
            ownerCompletedAdapter.notifyItemInserted(userPropertiesListCompleted.size - 1)
            ownerCompletedViewModel.agentOwnerCompletedPropertiesListDetails(page.toString(), "1")
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerCompletedViewModel = OwnerCompletedViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        ownerCompletedViewModel.getAgentOwnerCompletedPropertiesListDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (page == 1) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        if (page == 1) {
                            dismissProgress()
                        }
                        if (it.data != null) {
                            Log.e("response", Gson().toJson(it))
                            if (!it.data.tour_data.equals(null)) {
                                if (!(it.data.tour_data.user_properties.isNullOrEmpty())) {
                                    totalPages = it.data.tour_data.total_page_count
                                    if (userPropertiesListCompleted.size != 0) {
                                        isLoading = false
                                        page += 1
                                        userPropertiesListCompleted.removeAt(
                                            userPropertiesListCompleted.size - 1
                                        )
                                        ownerCompletedAdapter.notifyItemRemoved(
                                            userPropertiesListCompleted.size
                                        )
                                        userPropertiesListCompleted.addAll(it.data.tour_data.user_properties)
                                        ownerCompletedAdapter.notifyDataSetChanged()
                                    } else {
                                        if (it.data.tour_data.user_properties.isNotEmpty()) {
                                            page += 1
                                            userPropertiesListCompleted =
                                                it.data.tour_data.user_properties as ArrayList<UserProperty>
                                            setupOwnerCompletedRecyclerView()
                                        } else {
                                            llEmptyData.isVisible = true
                                            OwnerCompletedfrgRecyclerView.isVisible = false
                                        }
                                    }
                                } else {
                                    llEmptyData.isVisible = true
                                    OwnerCompletedfrgRecyclerView.isVisible = false
                                }
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
            }
    }

    override fun onClicks() {

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
                        phone,
                        message
                    )
                )
            )
        )
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            ownerCompletedViewModel.agentOwnerCompletedPropertiesListDetails(page.toString(), "1")
        }
    }
}
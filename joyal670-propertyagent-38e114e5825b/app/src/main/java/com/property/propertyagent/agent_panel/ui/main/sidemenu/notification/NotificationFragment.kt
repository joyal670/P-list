package com.property.propertyagent.agent_panel.ui.main.sidemenu.notification

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
import com.property.propertyagent.agent_panel.ui.main.sidemenu.notification.adapter.NotificationAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_notification_list.Notification
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_notification.*

@Suppress("MoveLambdaOutsideParentheses")
class NotificationFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var notificationViewModel: NotificationViewModel

    private var passedPosition = -1

    private var notificationList = ArrayList<Notification>()
    private lateinit var notificationAdapter: NotificationAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_notification, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.notification))

        notificationViewModel.agentNotificationList(page.toString())
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        notificationViewModel = NotificationViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        notificationViewModel.getAgentNotificationListResponse()
            .observe(this, {
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
                        if (!it.data!!.data.notifications.equals(null)) {
                            totalPages = it.data.data.total_page_count
                            if (notificationList.size != 0) {
                                isLoading = false
                                page += 1
                                notificationList.removeAt(notificationList.size - 1)
                                notificationAdapter.notifyItemRemoved(notificationList.size)
                                notificationList.addAll(it.data.data.notifications)
                                notificationAdapter.notifyDataSetChanged()
                            } else {
                                if (it.data.data.notifications.isNotEmpty()) {
                                    llEmptyData.isVisible = false
                                    page += 1
                                    notificationList =
                                        it.data.data.notifications as ArrayList<Notification>
                                    setNotificationSeeAllRecyclerView()
                                    container.isVisible = true
                                } else {
                                    llEmptyData.isVisible = true
                                    container.isVisible = false
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
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

        notificationViewModel.getAgentNotificationActionResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        page = 1
                        notificationList.clear()
                        notificationAdapter.notifyDataSetChanged()
                        notificationViewModel.agentNotificationList(page.toString())
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
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

    private fun setNotificationSeeAllRecyclerView() {
        notificationfrgRecycerview.layoutManager = LinearLayoutManager(context)
        notificationAdapter = NotificationAdapter(notificationList,
            { id, type, pos -> notificationActions(id, type, pos) })
        notificationfrgRecycerview.adapter = notificationAdapter

        notificationfrgRecycerview.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            notificationList.add(Notification("", -1, "", "", ""))
            notificationAdapter.notifyItemInserted(notificationList.size - 1)
            notificationViewModel.agentNotificationList(page.toString())
        }
    }

    private fun notificationActions(id: String, type: String, pos: Int) {
        passedPosition = pos
        notificationViewModel.agentNotificationAction(id, type)
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            notificationViewModel.agentNotificationList(page.toString())
        }
    }
}
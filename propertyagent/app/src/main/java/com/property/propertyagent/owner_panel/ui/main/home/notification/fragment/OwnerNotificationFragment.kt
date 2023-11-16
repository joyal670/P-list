package com.property.propertyagent.owner_panel.ui.main.home.notification.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.owner.owner_notification.OwnerNotificationNotification
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.owner_panel.ui.main.home.notification.adapter.OwnerNotificationAdapter
import com.property.propertyagent.owner_panel.ui.main.home.notification.viewmodel.OwnerNotificationViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_notification_owner.*

class OwnerNotificationFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerNotificationViewModel: OwnerNotificationViewModel
    private lateinit var ownerNotificationAdapter: OwnerNotificationAdapter
    private var notificationList = ArrayList<OwnerNotificationNotification>()
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_notification_owner, container, false)
    }

    override fun initData() {
        swipeRefreshLayout.setRefreshing(false)

        fragmentTransInterface = activity as HomeOwnerActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.notification))

        ownerNotificationViewModel.notification(page.toString())
    }

    private fun setRecyclerView() {
        notificationfrgOwnerRecyclerview.layoutManager = LinearLayoutManager(context)
        ownerNotificationAdapter = OwnerNotificationAdapter(requireContext(), notificationList)
        notificationfrgOwnerRecyclerview.adapter = ownerNotificationAdapter
        notificationfrgOwnerRecyclerview.scheduleLayoutAnimation()

        notificationfrgOwnerRecyclerview.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            notificationList.add(OwnerNotificationNotification("", "", -1, -1, ""))
            ownerNotificationAdapter.notifyItemInserted(notificationList.size - 1)
            ownerNotificationViewModel.notification(page.toString())
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerNotificationViewModel = OwnerNotificationViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        ownerNotificationViewModel.getOwnerNotificationResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (notificationList.size == 0) {
                        dismissProgressOwner()

                        if (it.data!!.data != null) {
                            totalPages = it.data.data.total_page_count
                            if (notificationList.size != 0) {
                                isLoading = false
                                page += 1
                                notificationList.removeAt(notificationList.size - 1)
                                ownerNotificationAdapter.notifyItemRemoved(notificationList.size)
                                notificationList.addAll(it.data.data.notifications)
                                ownerNotificationAdapter.notifyDataSetChanged()
                            } else {
                                page += 1
                                notificationList =
                                    it.data.data.notifications as ArrayList<OwnerNotificationNotification>
                                setRecyclerView()
                            }

                            llEmptyDataNotification.isVisible = notificationList.size == 0
                        }

                    }
                }
                Status.LOADING -> {
                    if (notificationList.size == 0) {
                        showProgressOwner()
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (requireActivity().isConnected) {
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
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
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
        notificationList.clear()
        ownerNotificationAdapter.notifyDataSetChanged()
        initData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_addProperty)
        item2.isVisible = false

        val item3: MenuItem = menu.findItem(R.id.customtoolbar_lineView)
        item3.isVisible = false

    }

}
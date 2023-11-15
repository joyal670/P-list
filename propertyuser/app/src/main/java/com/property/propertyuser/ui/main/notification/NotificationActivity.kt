package com.property.propertyuser.ui.main.notification

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityNotificationBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.user_notification.Notification
import com.property.propertyuser.ui.main.notification.adapter.NotificationAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.layout_no_notification_found.*

class NotificationActivity : BaseActivity<ActivityNotificationBinding>(), ActivityListener {

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationViewModel: NotificationViewModel
    private var isLoading: Boolean = false
    private var i = 0
    private var totalPageCount = 0
    private var notificationList = ArrayList<Notification>()
    private lateinit var layoutManager: LinearLayoutManager
    override val layoutId: Int
        get() = R.layout.activity_notification
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        i = 1
        setTitle(getString(R.string.notification))
        notificationList = ArrayList()
        notificationViewModel.fetchNotification(i.toString())
    }

    private fun setupRecyclerViewNotification() {
        layoutManager = LinearLayoutManager(this)
        rvNotificationList.layoutManager = layoutManager
        notificationAdapter =
            NotificationAdapter(this, notificationList, { id -> readNotification(id) })
        rvNotificationList.adapter = notificationAdapter
        /*rvNotificationList.startLayoutAnimation()*/

        rvNotificationList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    private fun readNotification(id: Int) {
        notificationViewModel.fetchReadNotificationUpdate(id.toString())
    }

    fun loadData() {
        if (i <= totalPageCount && this.isConnected) {
            notificationList.add(Notification("", -1, "", "", ""))
            notificationAdapter.notifyItemInserted(notificationList.size - 1)
            notificationViewModel.fetchNotification(i.toString())
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        notificationViewModel = NotificationViewModel()
    }

    override fun setupObserver() {
        notificationViewModel.getNotificationResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (notificationList.size == 0) {
                        showLoader()
                    }
                }
                Status.SUCCESS -> {
                    if (notificationList.size == 0) {
                        dismissLoader()
                    }
                    Log.e("responsecheck", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.data != null) {
                            totalPageCount = it.data.data.total_page_count
                            if (notificationList.size != 0) {
                                isLoading = false
                                i += 1
                                notificationList.removeAt(notificationList.size - 1)
                                notificationAdapter.notifyItemRemoved(notificationList.size)
                                notificationList.addAll(it.data.data.notifications)
                                notificationAdapter.notifyDataSetChanged()
                            } else {
                                if (!(it.data.data.notifications.isNullOrEmpty())) {
                                    includeNoInternet.visibility = View.GONE
                                    rvNotificationList.visibility = View.VISIBLE
                                    linearNoDataFoundNotification.visibility = View.GONE
                                    i += 1
                                    notificationList =
                                        it.data.data.notifications as ArrayList<Notification>
                                    setupRecyclerViewNotification()
                                } else {
                                    linearNoDataFoundNotification.visibility = View.VISIBLE
                                    rvNotificationList.visibility = View.GONE
                                }
                            }
                        }
                    }

                }
                Status.ERROR -> {
                    dismissLoader()
                    Toaster.showToast(
                        this, getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        rvNotificationList.visibility = View.GONE
                        /*Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)*/
                    }
                }

            }
        })

        notificationViewModel.getReadNotificationUpdateResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    // showLoader()
                }
                Status.SUCCESS -> {
                    // dismissLoader()
                    Toaster.showToast(
                        this, it.data!!.message,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG
                    )

                }
                Status.DATA_EMPTY -> {
                   // dismissLoader()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                   // dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                i = 1
                includeNoInternet.visibility = View.GONE
                rvNotificationList.visibility = View.VISIBLE
                notificationViewModel.fetchNotification(i.toString())
            }
        }

        retryBtn.setOnClickListener {
            initData()
        }
    }

    override fun getViewBinging(): ActivityNotificationBinding {
        return ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {
    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
package com.proteinium.proteiniumdietapp.ui.main.home.home.notification

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.home.notification.adapter.NotificationAdapter
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isConnected
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.no_subscrption.*
import kotlinx.android.synthetic.main.toolbar_notification.*

class NotificationActivity : BaseActivity() {
    private lateinit var notificationViewModel: NotificationViewModel
    override val layoutId: Int
        get() = R.layout.activity_notification
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData()
    {
        AppPreferences.isNotificationRead=false
        setSupportActionBar(toolbar_notification)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvToolbarTitle_notification.text = getString(R.string.tvNotifications)
        tvNoSubscription.text=getString(R.string.no_notification)
        AppPreferences.user_id?.let { notificationViewModel.fetchNotifications(it) }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
    }

    override fun setupObserver() {
        notificationViewModel.getNotificationsResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    noInternetLayoutNotification.visibility=View.GONE
                    if (it.data?.status!!) {
                        if (it.data.data != null) {
                            if (it.data.data.notifications.size > 0) {
                                rvNotification.visibility = View.VISIBLE
                                noNotificationLayout.visibility = View.GONE
                                rvNotification.layoutManager = LinearLayoutManager(this)
                                rvNotification.adapter =
                                    NotificationAdapter(it.data.data.notifications) { selectedNotificationId(it) }
                            } else {
                                rvNotification.visibility = View.GONE
                                noNotificationLayout.visibility = View.VISIBLE
                            }
                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    if(this.isConnected){
                        CommonUtils.warningToast(this,  getString(R.string.something_wrong))


                    }else{
                        noInternetLayoutNotification.visibility=View.VISIBLE
                        rvNotification.visibility=View.GONE
                    }
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this,  getString(R.string.data_empty))
                }
            }
        })

        notificationViewModel.getUpdateNotificationStatusResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.status!!) {
                        Toast.makeText(this,getString(R.string.read),Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this,  getString(R.string.data_empty))
                }
            }
        })
    }

    private fun selectedNotificationId(id: Int) {
        notificationViewModel.updateNotificationStatus(id)
    }

    override fun onClicks() {
        btnRetry.setOnClickListener {
            AppPreferences.user_id?.let { it1 -> notificationViewModel.fetchNotifications(it1) }
        }
    }
}


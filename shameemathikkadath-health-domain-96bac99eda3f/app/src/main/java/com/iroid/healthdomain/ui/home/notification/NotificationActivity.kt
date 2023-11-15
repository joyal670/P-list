package com.iroid.healthdomain.ui.home.notification

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActivityNotificationBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.notification.fragments.NotificationReceivedFragment
import com.iroid.healthdomain.ui.home.notification.fragments.NotificationSentFragment
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationActivity : BaseActivity<NotificationViewModel,ActivityNotificationBinding,HomeRepository>(){

    companion object {
        private const val TAG = "Notification Activity"
    }

    private var fragmentList = arrayListOf<Fragment>()
    private lateinit var adaptor: ViewPagerAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)

        generateViewPager()

        GlobalScope.launch {
            userPreferences.saveNotification(false)
        }
    }

    private fun generateViewPager() {
        fragmentList.clear()
        fragmentList.add(NotificationReceivedFragment())
        fragmentList.add(NotificationSentFragment())

        adaptor = ViewPagerAdaptor(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )
        binding.notificationViewPager.adapter = adaptor


        TabLayoutMediator(binding.notificationTabLayout, binding.notificationViewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "Received"
                1 -> tab.text = "Sent"
            }

        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutRes(): Int = R.layout.activity_notification

    override fun getViewModel(): Class<NotificationViewModel> = NotificationViewModel::class.java

    override fun init() {

    }

    override fun getFragmentRepository(): HomeRepository {
        return HomeRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }
}
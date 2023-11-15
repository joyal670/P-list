package com.property.propertyagent.start_up.no_internet

import android.content.Context
import android.content.Intent
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_no_internet.*

class NoInternetActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_no_internet
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    fun start(context: Context?): Intent {
        return Intent(context, NoInternetActivity::class.java)
    }

    override fun fragmentLaunch() {

    }

    override fun initData() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        bt_try_again.setOnClickListener {
            if (applicationContext.isConnected) {
                isConnectionRestored = true
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
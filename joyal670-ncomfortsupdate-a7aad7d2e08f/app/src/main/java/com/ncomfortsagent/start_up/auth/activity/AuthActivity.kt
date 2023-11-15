package com.ncomfortsagent.start_up.auth.activity

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityAuthBinding
import com.ncomfortsagent.listeners.ActivityListener
import com.ncomfortsagent.start_up.auth.fragment.LoginFragment
import com.ncomfortsagent.ui.main.home.home.activity.HomeActivity
import com.ncomfortsagent.utils.AppPreferences.prefIsLogin

import com.ncomfortsagent.utils.replaceFragment

class AuthActivity : BaseActivity<ActivityAuthBinding>(), ActivityListener {


    override val layoutId: Int
        get() = R.layout.activity_auth
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)

    override fun initData() {
        fragmentLaunch()
    }

    override fun fragmentLaunch() {
        replaceFragment(fragment = LoginFragment())
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    override fun onResume() {
        super.onResume()
        if (prefIsLogin) {
            startActivity(Intent(this, HomeActivity::class.java))
            ActivityCompat.finishAffinity(this)
        }
    }
}
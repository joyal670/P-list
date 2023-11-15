package com.property.propertyuser.ui.startup.auth

import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityAuthBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.startup.auth.login.LoginFragment
import com.property.propertyuser.utils.replaceFragment

class AuthActivity :BaseActivity<ActivityAuthBinding>() ,ActivityListener {
    override fun getViewBinging(): ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_auth
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        replaceFragment(fragment = LoginFragment())
    }

    override fun initData() {
        fragmentLaunch()
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
        replaceFragment(fragment=fragment,addToBackStack = true)
    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
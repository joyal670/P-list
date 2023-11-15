package com.mytodo.mytodo.start_up.auth.activity


import androidx.fragment.app.Fragment
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseActivity
import com.mytodo.mytodo.databinding.ActivityMainBinding
import com.mytodo.mytodo.listeners.ActivityListener
import com.mytodo.mytodo.start_up.auth.fragment.LoginFragment
import com.mytodo.mytodo.utils.replaceFragment

class AuthActivity : BaseActivity<ActivityMainBinding>(), ActivityListener {
    override val layoutId: Int
        get() =  R.layout.activity_main
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

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

}
package com.proteinium.proteiniumdietapp.ui.start_up.auth

import androidx.fragment.app.Fragment
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.ui.start_up.auth.login.LoginFragment
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_TYPE
import com.proteinium.proteiniumdietapp.utils.Constants.TYPE_GUEST
import com.proteinium.proteiniumdietapp.utils.replaceFragment

class AuthActivity : BaseActivity(), ActivityListener
{
    override val layoutId: Int
        get() = R.layout.activity_auth
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false
    private var isGuest=false

    override fun fragmentLaunch() {
        replaceFragment(fragment = LoginFragment.newInstance(isGuest))

    }

    override fun initData() {
        isGuest = intent.getStringExtra(INTENT_TYPE)==TYPE_GUEST
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

    override fun setTitle(title: String, size: Float, fontFamily: Int, textAllCaps: Boolean) {

    }

    override fun setBackButton(backEnabled: Boolean) {

    }

    override fun hideToolbar(hideToolbar: Boolean) {

    }


}
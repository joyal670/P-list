package com.property.propertyuser.ui.startup.auth.otp

import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityOtpBinding
/*import com.property.propertyuser.data.api.ApiHelperImpl
import com.property.propertyuser.data.api.RetrofitBuilder*/
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.utils.Constants.INTENT_MOBILE
import com.property.propertyuser.utils.Constants.INTENT_OTP
/*import com.property.propertyuser.utils.ViewModelFactory*/
import com.property.propertyuser.utils.replaceFragment

class OtpActivity:BaseActivity<ActivityOtpBinding>(), ActivityListener {
    override fun getViewBinging(): ActivityOtpBinding = ActivityOtpBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_otp
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        val mobile=intent.getStringExtra(INTENT_MOBILE)
        val otp=intent.getStringExtra(INTENT_OTP)
        replaceFragment(fragment = OtpFragment.newInstance(otp.toString(), mobile.toString()))
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

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }
}
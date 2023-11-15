package com.iroid.patrickstore.ui.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityForgotPasswordBinding
import com.iroid.patrickstore.databinding.FragmentMobileNumberBinding
import com.iroid.patrickstore.ui.forgotpassword.numberverfication.MobileFragment
import com.iroid.patrickstore.ui.forgotpassword.viewmodel.ForgotViewModal
import com.iroid.patrickstore.ui.my_account.profile.ProfileFragment
import com.iroid.patrickstore.utils.Constants
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity:BaseActivity<ForgotViewModal,ActivityForgotPasswordBinding>() {
    private lateinit var forgotViewModal: ForgotViewModal
    override val layoutId: Int
        get() = R.layout.activity_forgot_password
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.forgotpassword)
    }

    override fun getViewBinding(): ActivityForgotPasswordBinding {
        return ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun initViews() {
    }


}

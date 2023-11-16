package com.iroid.patrickstore.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityLoginBinding
import com.iroid.patrickstore.model.login.LoginResponse
import com.iroid.patrickstore.preference.AppPreferences.email
import com.iroid.patrickstore.preference.AppPreferences.first_name
import com.iroid.patrickstore.preference.AppPreferences.image_url
import com.iroid.patrickstore.preference.AppPreferences.isLogin
import com.iroid.patrickstore.preference.AppPreferences.jwt_token
import com.iroid.patrickstore.preference.AppPreferences.last_name
import com.iroid.patrickstore.preference.AppPreferences.mobile
import com.iroid.patrickstore.ui.forgotpassword.ForgotPasswordActivity
import com.iroid.patrickstore.ui.location.LocationActivity
import com.iroid.patrickstore.ui.signup.SignUpActivity
import com.iroid.patrickstore.utils.*

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override val setToolbar: Boolean
        get() = false

    override val hideStatusBar: Boolean
        get() = false

    override val layoutId: Int
        get() = R.layout.activity_login

    private fun setOnClick() {
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
        binding.tvRecoverPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun setOnTextChanged() {
        binding.editUserName.doOnTextChanged { text, start, before, count ->
            viewModel.onUserNameTextChanged(text.toString())
        }
        binding.editPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onPasswordTextChanged(text.toString())
        }
    }

    private fun setUpObserver() {
        viewModel.loginLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.message == "")
                        dismissProgress()
                    setUpUserData(it.data!!)

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
        viewModel.emptyEmail?.observe(this) { message ->
            if (message != null)
                getViewBinding().tilUserName.error = message
            else
                getViewBinding().tilUserName.isErrorEnabled = false

        }

        viewModel.emptyPassword?.observe(this) { message ->
            if (message != null)
                getViewBinding().tilPassword.error = message
            else
                getViewBinding().tilPassword.isErrorEnabled = false
        }
    }

    private fun setUpUserData(data: LoginResponse) {
        jwt_token = data.data.jwtToken
        first_name = data.data.firstName
        last_name = data.data.lastName
        email = data.data.email
        mobile = data.data.mobile
        if (!data.data.imageUrl.isNullOrEmpty()) {
            image_url = data.data.imageUrl
        }
        isLogin = true
        startActivity(Intent(this, LocationActivity::class.java))
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setOnTextChanged()
        setUpObserver()
        setOnClick()
    }

}

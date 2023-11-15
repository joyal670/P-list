package com.iroid.patrickstore.ui.forgotpassword.otp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityOtpBinding
import com.iroid.patrickstore.model.otp.OtpData
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.location.LocationActivity
import com.iroid.patrickstore.ui.login.LoginActivity
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_otp.view.*


class OtpActivity:BaseActivity<OtpViewModel,ActivityOtpBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_otp
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false



    private fun setOnClick() {
        binding.btnVerify.setOnClickListener {
            viewModel.otpVerify(binding.otpView.otp)
        }
    }

    private fun setUpObserver() {
        viewModel.otpLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    setLoginData(it.data!!.data)
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(this,it.message!!,Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    if(this.isConnected){
                        Toaster.showToast(this, SOMETHING_WENT_WRONG, Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this, NO_INTERNET_MESSAGE, Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }
            }
        })
    }

    private fun setLoginData(data: OtpData) {
        AppPreferences.jwt_token =data.jwtToken
        AppPreferences.last_name =data.lastName
        AppPreferences.first_name =data.firstName
        AppPreferences.email =data.email
        AppPreferences.mobile =data.mobile
        if(!data.imageUrl.isNullOrEmpty()){
            AppPreferences.image_url =data.imageUrl
        }
        AppPreferences.isLogin =true
        startActivity(Intent(this,  LoginActivity::class.java))

    }

    override fun getViewBinding(): ActivityOtpBinding {
       return ActivityOtpBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        Log.e("123456", "initViews: ${intent.getStringExtra(Constants.INTENT_OTP)}" )
        binding.otpView.otp=intent.getStringExtra(Constants.INTENT_OTP)
        setOnClick()
        setUpObserver()
    }


}

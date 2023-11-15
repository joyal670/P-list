package com.ncomfortsagent.start_up.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivitySplashBinding
import com.ncomfortsagent.start_up.auth.activity.AuthActivity
import com.ncomfortsagent.utils.AppPreferences
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.Constants
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>()
{
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun initData() {

        if(AppPreferences.user_lang== Constants.ARABIC_LAG){
            CommonUtils.changeLanguageAwareContext(this,Constants.ARABIC_LAG)
        }else{
            CommonUtils.changeLanguageAwareContext(this,Constants.ENGLISH_LAG)
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashActivity , AuthActivity::class.java))
                finishAffinity()
            }
        } , 3000)
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }


}
package com.proteinium.proteiniumdietapp.ui.start_up.splash

import android.content.Intent
import android.util.Log
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.start_up.language_selection.LangaugeSelectionActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import java.util.*

class SplashActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun fragmentLaunch() {

    }

    override fun initData()
    {
        if(AppPreferences.chooseLanguage == Constants.ARABIC_LAG){
            CommonUtils.changeLanguageAwareContext(this, Constants.ARABIC_LAG)
        }
        if(AppPreferences.chooseLanguage == Constants.ENGLISH_LAG){
            CommonUtils.changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
        }
        Timer().schedule(object : TimerTask()
        {

            override fun run() {
                if(AppPreferences.isLanguageSelected){
                    if(AppPreferences.isLogin){
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashActivity, LangaugeSelectionActivity::class.java))
                        finish()
                    }
                }else{
                    startActivity(Intent(this@SplashActivity, LangaugeSelectionActivity::class.java))
                    finish()
                }

            }
        }, 3500)
        Log.e("TAG", "initData: "+"splash2" )
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

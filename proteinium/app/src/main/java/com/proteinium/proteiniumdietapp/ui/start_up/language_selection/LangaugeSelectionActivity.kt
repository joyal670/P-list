package com.proteinium.proteiniumdietapp.ui.start_up.language_selection

import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import kotlinx.android.synthetic.main.activity_langauge_selection.*

class LangaugeSelectionActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_langauge_selection
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun fragmentLaunch() {

    }

    override fun initData() {
        english_Btn.setTextColor(Color.parseColor("#5DA7A3"))
        arabic_Btn.setTextColor(Color.parseColor("#5DA7A3"))
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks()
    {
        english_Btn.setOnClickListener {
            AppPreferences.chooseLanguage=Constants.ENGLISH_LAG
            AppPreferences.isLanguageSelected=true
            english_Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_main)
            english_Btn.setTextColor(Color.parseColor("#FFFFFF"))
            arabic_Btn.setTextColor(Color.parseColor("#5DA7A3"))
            arabic_Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            CommonUtils.changeLanguageAwareContext(this,Constants.ENGLISH_LAG)
            redirectActivity()
        }

        arabic_Btn.setOnClickListener {
            AppPreferences.chooseLanguage=Constants.ARABIC_LAG
            AppPreferences.isLanguageSelected=true
            arabic_Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_main)
            arabic_Btn.setTextColor(Color.parseColor("#FFFFFF"))
            english_Btn.setTextColor(Color.parseColor("#5DA7A3"))
            english_Btn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            CommonUtils.changeLanguageAwareContext(this,Constants.ARABIC_LAG)
            redirectActivity()
        }
    }

    private fun redirectActivity() {
        if(AppPreferences.isLanguageSelected){
            if(AppPreferences.isLogin){
                val intent=Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                finish()
            }else{
                startActivity(Intent(this, AuthActivity::class.java))



            }
        }
    }

}
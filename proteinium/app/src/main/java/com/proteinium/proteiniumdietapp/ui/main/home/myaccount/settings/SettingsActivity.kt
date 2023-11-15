package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.settings

import android.content.Intent
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.chooseLanguage
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isLogin
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.*
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_TYPE
import com.proteinium.proteiniumdietapp.utils.Constants.TYPE_GUEST

import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class SettingsActivity : BaseActivity() {
    private lateinit var settingViewModel: SettingViewModel
    private var isLoaded=false
    val pakeges = arrayOf("Arabic", "English")

    override val layoutId: Int
        get() = R.layout.activity_settings
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage==Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvSettings)
        if(AppPreferences.isLogin){
            settingViewModel.getPushStatus(user_id!!)
        }else{
            switchPushNotification.isEnabled=true
        }

        if(chooseLanguage=="ar"){
            tvLanguage.text = getString(R.string.arabic)
        }
        if(chooseLanguage=="en"){
            tvLanguage.text = getString(R.string.english)
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        settingViewModel = SettingViewModel()
    }

    override fun setupObserver() {
        settingViewModel.getPushStatusLiveData().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data!!.data.notification_status == 0) {
                        switchPushNotification.isChecked = false
                    }
                    if (it.data!!.data.notification_status == 1) {
                        switchPushNotification.isChecked = true
                    }
                    switchPushNotification.isEnabled=true
                    isLoaded=true

                }
                Status.ERROR -> {
                    dismissLoader()
                    if (this.isConnected) {
                        CommonUtils.warningToast(this,  getString(R.string.something_wrong))
                    } else {
                        CommonUtils.warningToast(this,  getString(R.string.no_internet))
                    }

                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this,  it.data!!.message)
                }
            }
        })
        settingViewModel.getPushLiveData().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.data!!.message)

                }


                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.data!!.message)
                }
            }
        })
    }

    override fun onClicks() {
        switchPushNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isLogin){
                if(isLoaded){
                    if(isChecked){
                        settingViewModel.setPush(user_id!!,1)
                    }else{
                        settingViewModel.setPush(user_id!!,0)
                    }
                }
            }else{
                val intent=Intent(this,AuthActivity::class.java)
                intent.putExtra(INTENT_TYPE,TYPE_GUEST)
                startActivity(intent)
            }


        }
        setting_spinner.setOnClickListener {
            val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_language, null)

            val arabicBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectArabic)
            val englishBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectEnglish)
            if(chooseLanguage=="ar"){
                arabicBtn.isChecked=true
                englishBtn.isChecked=false
            }
            if(chooseLanguage=="en"){
                arabicBtn.isChecked=false
                englishBtn.isChecked=true
            }
            val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseSelectlanguage)
            close.setOnClickListener {
                bottom.dismiss()
            }

            val selectBtn = bottomSheet.findViewById<MaterialButton>(R.id.SelectLanguageBtn)

            selectBtn.setOnClickListener {
                if (arabicBtn.isChecked) {
                    tvLanguage.setTextColor(ContextCompat.getColor(this, R.color.black))
                    tvLanguage.text = getString(R.string.arabic)
                    AppPreferences.chooseLanguage=Constants.ARABIC_LAG
                    CommonUtils.changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                    bottom.dismiss()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else if (englishBtn.isChecked) {
                    tvLanguage.setTextColor(ContextCompat.getColor(this, R.color.black))
                    tvLanguage.text = getString(R.string.english)
                    AppPreferences.chooseLanguage=Constants.ENGLISH_LAG
                    CommonUtils.changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                    bottom.dismiss()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Select language", Toast.LENGTH_SHORT).show()
                }


            }

            bottom.setContentView(bottomSheet)
            bottom.show()
        }

    }

}
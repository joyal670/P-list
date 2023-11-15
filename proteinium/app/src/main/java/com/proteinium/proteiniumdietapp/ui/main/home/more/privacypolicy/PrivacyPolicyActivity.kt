package com.proteinium.proteiniumdietapp.ui.main.home.more.privacypolicy

import android.os.Build
import android.text.Html
import android.view.View
import androidx.lifecycle.Observer
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isConnected
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.wvAbout
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class PrivacyPolicyActivity : BaseActivity() {
    private lateinit var privacyPolicyModel: PrivacyPolicyModel
    override val layoutId: Int
        get() = R.layout.activity_privacy_policy
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if(AppPreferences.chooseLanguage== Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvPrivacyPolicy)
        //setSupportActionBar(toolbarSub)
        privacyPolicyModel.fetchAbout()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        privacyPolicyModel=PrivacyPolicyModel()
    }

    override fun setupObserver() {
        privacyPolicyModel.getPrivacyResponse().observe(this, Observer {aboutResponse->
            when(aboutResponse.status){
                Status.SUCCESS->{
                    dismissLoader()
                    noInternetLayoutPrivacy.visibility=View.GONE
                    llPrivacyPolicy.visibility= View.VISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wvAbout.text =
                            Html.fromHtml(aboutResponse.data?.data?.description.toString(), Html.FROM_HTML_MODE_LEGACY);
                    } else {
                        wvAbout.text = Html.fromHtml(aboutResponse.data?.data?.description.toString())
                    }
                }
                Status.LOADING->{
                    showLoader()

                }
                Status.ERROR->{
                    dismissLoader()
                     if(this.isConnected){
                         CommonUtils.warningToast(this, getString(R.string.something_wrong))

                    }else{
                    noInternetLayoutPrivacy.visibility=View.VISIBLE
                    llPrivacyPolicy.visibility= View.GONE
                    }

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, aboutResponse.message!!)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    noInternetLayoutPrivacy.visibility=View.VISIBLE
                    llPrivacyPolicy.visibility= View.GONE
                }

            }

        })

    }

    override fun onClicks() {
        btnRetry.setOnClickListener {
            privacyPolicyModel.fetchAbout()
        }
    }

}
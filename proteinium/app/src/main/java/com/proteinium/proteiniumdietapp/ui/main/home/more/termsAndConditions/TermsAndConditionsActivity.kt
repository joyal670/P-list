

package com.proteinium.proteiniumdietapp.ui.main.home.more.termsAndConditions

import android.content.res.Configuration
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.*
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class TermsAndConditionsActivity : BaseActivity() {
    private lateinit var termsAndConditionsViewModel: TermsAndConditionsViewModel
    override val layoutId: Int
        get() = R.layout.activity_terms_and_conditions
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        Log.e("123456", "initData:" + AppPreferences.chooseLanguage)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            tvSubToolbarTitle.text = getString(R.string.tvTermsAndConditions)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tvSubToolbarTitle.text = getString(R.string.tvTermsAndConditions)
        termsAndConditionsViewModel.fetchAbout()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        termsAndConditionsViewModel = TermsAndConditionsViewModel()
    }

    //
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        CommonUtils.changeLanguageAwareContext(this, AppPreferences.chooseLanguage!!)

    }

    override fun setupObserver() {
        termsAndConditionsViewModel.getAboutResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    noInternetLayoutTerms.visibility = View.GONE
                    llTermsMain.visibility = View.VISIBLE
                    if (it.data?.status!!) {
                        if (it.data.data != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                termsAndCondition_webView.text =
                                    Html.fromHtml(it.data?.data?.description.toString(), Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                termsAndCondition_webView.text = Html.fromHtml(it.data?.data?.description.toString())
                            }


                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    if (this.isConnected) {
                        CommonUtils.warningToast(this, getString(R.string.something_wrong))

                    } else {
                        noInternetLayoutTerms.visibility = View.VISIBLE
                        llTermsMain.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this,it.message!!)
                }
            }
        })
    }

    override fun onClicks() {
        btnRetry.setOnClickListener {
            termsAndConditionsViewModel.fetchAbout()
        }
    }

}
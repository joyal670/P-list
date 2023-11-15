package com.proteinium.proteiniumdietapp.ui.main.home.more.aboutus

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class AboutUsActivity : BaseActivity() {
    private lateinit var aboutViewModel: AboutViewModel
    override val layoutId: Int
        get() = R.layout.activity_about_us
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
        tvSubToolbarTitle.text = getString(R.string.tvAboutUs)
        aboutViewModel.fetchAbout()

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        aboutViewModel= AboutViewModel()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setupObserver() {
        aboutViewModel.getAboutResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    noInternetLayoutAbout.visibility=View.GONE
                    llAboutMain.visibility=View.VISIBLE
                 if (it.data?.status!!) {
                        if (it.data.data != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wvAbout.text =
                                    Html.fromHtml(it.data?.data?.description.toString(), Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                wvAbout.text = Html.fromHtml(it.data?.data?.description.toString())
                            }

                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                     if(this.isConnected){
                         CommonUtils.warningToast(this, getString(R.string.something_wrong))

                    }else{
                        noInternetLayoutAbout.visibility=View.VISIBLE
                    llAboutMain.visibility=View.GONE
                    }
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message!!)
                }
            }
        })

    }

    override fun onClicks() {
        btnRetry.setOnClickListener {
            aboutViewModel.fetchAbout()
        }
    }

}
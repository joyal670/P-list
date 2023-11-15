package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.activity

import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.Constants
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class InfoActivity:BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_web
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
        if(AppPreferences.chooseLanguage== Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.mcaros1)
        tvTerms.text=getString(R.string.mcaros1)
        termsAndCondition_webView.settings.javaScriptEnabled = true

        termsAndCondition_webView.loadUrl("https://www.nutritionix.com/database/common-foods")
        termsAndCondition_webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl("https://www.nutritionix.com/database/common-foods")
                return true
            }
        }
        termsAndCondition_webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@InfoActivity)
                val alertDialog: android.app.AlertDialog = builder.create()
                var message = "SSL Certificate error."
                when (error!!.primaryError) {
                    SslError.SSL_UNTRUSTED -> message =
                        "The certificate authority is not trusted."
                    SslError.SSL_EXPIRED -> message =
                        "The certificate has expired."
                    SslError.SSL_IDMISMATCH -> message =
                        "The certificate Hostname mismatch."
                    SslError.SSL_NOTYETVALID -> message =
                        "The certificate is not yet valid."
                }

                message += " Do you want to continue anyway?"
                alertDialog.setTitle("SSL Certificate Error")
                alertDialog.setMessage(message)
                alertDialog.setButton(
                    DialogInterface.BUTTON_POSITIVE,
                    "OK"
                ) { dialog, which -> // Ignore SSL certificate errors
                    handler!!.proceed()
                }

                alertDialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    "Cancel"
                ) { dialog, which -> handler!!.cancel() }
                alertDialog.show()
            }
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            }
        }

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
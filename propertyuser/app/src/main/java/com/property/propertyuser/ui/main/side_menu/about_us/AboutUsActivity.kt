package com.property.propertyuser.ui.main.side_menu.about_us

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityAboutUsBinding
import com.property.propertyuser.dialogs.CustomProgressDialog
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.layout_no_network.*

class AboutUsActivity : BaseActivity<ActivityAboutUsBinding>(),ActivityListener {
    private lateinit var customProgressDialog1: CustomProgressDialog
    override val layoutId: Int
        get() = R.layout.activity_about_us
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        customProgressDialog1=CustomProgressDialog()
        setTitle(getString(R.string.about_us))
        wvAboutActivity.settings.loadWithOverviewMode = true
        wvAboutActivity.requestFocus()
        wvAboutActivity.settings.allowFileAccess = true
        wvAboutActivity.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                customProgressDialog1.dialog.dismiss()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                customProgressDialog1.show(view!!.context)
            }
        }
        wvAboutActivity.settings.javaScriptEnabled = true
        if(this.isConnected){
            wvAboutActivity.visibility= View.VISIBLE
            includeNoInternetAbout.visibility=View.GONE
            linearNoDataFoundAbout.visibility=View.GONE
            if(AppPreferences.chooseLanguage.equals("english",true)){
                wvAboutActivity.
                loadUrl("http://admin.siaaha.com/show-user-about/1")
            }else{
                wvAboutActivity.
                loadUrl("http://admin.siaaha.com/show-user-about/2")
            }
        }
        else{
            wvAboutActivity.visibility= View.GONE
            includeNoInternetAbout.visibility=View.VISIBLE
            linearNoDataFoundAbout.visibility=View.GONE
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                wvAboutActivity.visibility= View.VISIBLE
                includeNoInternetAbout.visibility=View.GONE
                linearNoDataFoundAbout.visibility=View.GONE
                if(AppPreferences.chooseLanguage.equals("english",true)){
                    wvAboutActivity.
                    loadUrl("http://admin.siaaha.com/show-user-about/1")
                }else{
                    wvAboutActivity.
                    loadUrl("http://admin.siaaha.com/show-user-about/2")
                }
            }
        }
    }

    override fun getViewBinging(): ActivityAboutUsBinding {
        return ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
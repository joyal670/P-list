package com.property.propertyuser.ui.main.side_menu.privacy_policy

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPrivacyPolicyBinding
import com.property.propertyuser.dialogs.CustomProgressDialog
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.privacy_policy.PrivacyPolicy
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.side_menu.privacy_policy.adapter.PrivacyPolicyItemAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.layout_no_network.*

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>(),ActivityListener {
    private lateinit var privacyPolicyViewModel: PrivacyPolicyViewModel
    private lateinit var customProgressDialog1: CustomProgressDialog
    override val layoutId: Int
        get() = R.layout.activity_privacy_policy
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        false
    }

    override fun initData() {
        customProgressDialog1=CustomProgressDialog()
        setTitle(getString(R.string.privacy_policy))
        //privacyPolicyViewModel.fetchPrivacyPolicyListDetail("")
        wvPrivacyPolicyActivity.settings.loadWithOverviewMode = true
        wvPrivacyPolicyActivity.requestFocus()
        wvPrivacyPolicyActivity.settings.allowFileAccess = true
        wvPrivacyPolicyActivity.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                customProgressDialog1.dialog.dismiss()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                customProgressDialog1.show(view!!.context)
            }
        }
        wvPrivacyPolicyActivity.settings.javaScriptEnabled = true
        if(this.isConnected){
            linearPrivacy.visibility= View.VISIBLE
            includeNoInternet.visibility=View.GONE
            linearNoDataFound.visibility=View.GONE
            if(AppPreferences.chooseLanguage.equals("english",true)){
                wvPrivacyPolicyActivity.
                loadUrl("http://admin.siaaha.com/show-user-privacy/1")
            }else{
                wvPrivacyPolicyActivity.
                loadUrl("http://admin.siaaha.com/show-user-privacy/2")
            }
        }
        else{
            linearPrivacy.visibility= View.GONE
            includeNoInternet.visibility=View.VISIBLE
            linearNoDataFound.visibility=View.GONE
        }


    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        privacyPolicyViewModel= PrivacyPolicyViewModel()
    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                linearPrivacy.visibility= View.VISIBLE
                includeNoInternet.visibility=View.GONE
                linearNoDataFound.visibility=View.GONE
                if(AppPreferences.chooseLanguage.equals("english",true)){
                    wvPrivacyPolicyActivity.
                    loadUrl("http://admin.siaaha.com/show-user-privacy/1")
                }else{
                    wvPrivacyPolicyActivity.
                    loadUrl("http://admin.siaaha.com/show-user-privacy/2")
                }
            }
        }
    }

    override fun getViewBinging(): ActivityPrivacyPolicyBinding {
        return ActivityPrivacyPolicyBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
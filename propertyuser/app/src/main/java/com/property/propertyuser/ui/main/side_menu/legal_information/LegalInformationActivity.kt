package com.property.propertyuser.ui.main.side_menu.legal_information

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityLegalInformationBinding
import com.property.propertyuser.dialogs.CustomProgressDialog
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_legal_information.*
import kotlinx.android.synthetic.main.layout_no_network.*


class LegalInformationActivity : BaseActivity<ActivityLegalInformationBinding>(),ActivityListener {
    private lateinit var legalInformationViewModel: LegalInformationViewModel
    private lateinit var customProgressDialog1: CustomProgressDialog
    override val layoutId: Int
        get() = R.layout.activity_legal_information
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }
    /*private fun setLegalInformationRecyclerView(legalInformations: List<LegalInformation>) {
        rvLegalInformationList.layoutManager = LinearLayoutManager(this)
        rvLegalInformationList.adapter = LegalInformationItemAdapter(this,legalInformations)
    }*/
    override fun initData() {
        customProgressDialog1=CustomProgressDialog()
        setTitle(getString(R.string.legal_information))
        //legalInformationViewModel.fetchLegalInformationListDetail("")
        wvLegalInformationActivity.settings.loadWithOverviewMode = true
        wvLegalInformationActivity.requestFocus()
        wvLegalInformationActivity.settings.allowFileAccess = true
        wvLegalInformationActivity.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                customProgressDialog1.dialog.dismiss()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                customProgressDialog1.show(view!!.context)
            }
        }
        wvLegalInformationActivity.settings.javaScriptEnabled = true
        Log.e("check lang",AppPreferences.chooseLanguage.toString())
        if(this.isConnected){
            wvLegalInformationActivity.visibility= View.VISIBLE
            includeNoInternet.visibility=View.GONE
            linearNoDataFound.visibility=View.GONE
            if(AppPreferences.chooseLanguage.equals("english",true)){
                wvLegalInformationActivity.
                loadUrl("http://admin.siaaha.com/show-user-legal/1")
            }else{
                wvLegalInformationActivity.
                loadUrl("http://admin.siaaha.com/show-user-legal/2")
            }
        }
        else{
            wvLegalInformationActivity.visibility= View.GONE
            includeNoInternet.visibility=View.VISIBLE
            linearNoDataFound.visibility=View.GONE
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        legalInformationViewModel= LegalInformationViewModel()
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                wvLegalInformationActivity.visibility= View.VISIBLE
                includeNoInternet.visibility=View.GONE
                linearNoDataFound.visibility=View.GONE
                if(AppPreferences.chooseLanguage.equals("english",true)){
                    wvLegalInformationActivity.
                    loadUrl("http://admin.siaaha.com/show-user-legal/1")
                }else{
                    wvLegalInformationActivity.
                    loadUrl("http://admin.siaaha.com/show-user-legal/2")
                }
            }
        }
    }

    override fun getViewBinging(): ActivityLegalInformationBinding {
        return ActivityLegalInformationBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
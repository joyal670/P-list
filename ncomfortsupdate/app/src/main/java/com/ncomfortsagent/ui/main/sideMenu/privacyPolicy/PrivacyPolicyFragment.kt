package com.ncomfortsagent.ui.main.sideMenu.privacyPolicy

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentPrivacyPolicyBinding
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity
import com.ncomfortsagent.utils.AppPreferences
import com.ncomfortsagent.utils.Constants

class PrivacyPolicyFragment : BaseFragment()
{
    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as SideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.privacy_policy))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setupUI() {

        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgress()
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                dismissProgress()
                super.onPageFinished(view, url)
            }
        }

        binding.webView.clearView()
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webView.setInitialScale(1)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.pluginState = WebSettings.PluginState.ON
        binding.webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        binding.webView.webViewClient = webViewClient
        binding.webView.settings.defaultTextEncodingName = "utf-8"
        binding.webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.setPadding(0, 0, 0, 0)
        if(AppPreferences.user_lang== Constants.ARABIC_LAG){
            binding.webView.loadUrl("http://ncomfort.iroidtechnologies.in/show-user-privacy/2")
        }else{
            binding.webView.loadUrl("http://ncomfort.iroidtechnologies.in/show-user-privacy/1")
        }


    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

}
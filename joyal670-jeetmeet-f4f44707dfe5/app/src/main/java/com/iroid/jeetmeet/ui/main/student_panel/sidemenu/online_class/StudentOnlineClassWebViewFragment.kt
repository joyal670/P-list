package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.online_class

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentOnlineClassStartWebViewBinding


class StudentOnlineClassWebViewFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentOnlineClassStartWebViewBinding

    private var mPermissionRequest: PermissionRequest? = null

    private val REQUEST_CAMERA_PERMISSION = 1
    private val PERM_CAMERA = arrayOf(Manifest.permission.CAMERA)

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentStudentOnlineClassStartWebViewBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    companion object {

        private const val TAG = "StudentOnlineFragment"
        private var url: String? = null

        @JvmStatic
        fun newInstance(url: String) =
            StudentOnlineClassWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url")
            Log.e(TAG, "onCreate: $url")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {

        /* binding.webView.webViewClient = WebViewClient()
         binding.webView.loadUrl(url!!)
         binding.webView.settings.javaScriptEnabled = true
         binding.webView.settings.setSupportZoom(true)*/

        appCompatActivity!!.supportActionBar!!.hide()

        binding.webView.clearView()
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webView.setInitialScale(1)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.pluginState = WebSettings.PluginState.ON
        binding.webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.setPadding(0, 0, 0, 0)

        //appCompatActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                mPermissionRequest = request
                mPermissionRequest!!.grant(mPermissionRequest!!.resources)
            }
        }
        val data_html = url!!

        binding.webView.loadDataWithBaseURL(
            "https://jeetmeet.livebox.co.in/lbmeeting/?key=1246ba5e2d9af3650d81bd0b6192a9eb",
            data_html,
            "text/html",
            "UTF-8",
            null
        )
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
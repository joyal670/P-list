package com.property.propertyuser.ui.main.home.scanner

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityQrScannerBinding
import com.property.propertyuser.databinding.ActivitySplashBinding
import com.property.propertyuser.listeners.ActivityListener
import kotlinx.android.synthetic.main.activity_qr_scanner.*

class QrScannerActivity : BaseActivity<ActivityQrScannerBinding>(),ActivityListener {
    override fun getViewBinging(): ActivityQrScannerBinding = ActivityQrScannerBinding.inflate(layoutInflater)
    private var codeScanner: CodeScanner? = null
    override val layoutId: Int
        get() = R.layout.activity_qr_scanner
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
       setTitle("QR CODE SCANNER")
        methodWithPermissions()

    }
    // requesting run time permissions
    private fun methodWithPermissions()
    {
        permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
            if (result.allGranted()) {
                codeScanner = CodeScanner(this, scanner_view)
                codeScanner!!.decodeCallback = DecodeCallback {
                    this.runOnUiThread {
                        // toast the qr result
                        Toast.makeText(this, it.text, Toast.LENGTH_LONG).show()
                    }
                }
                // setup camera again on click
                scanner_view.setOnClickListener {
                    codeScanner!!.startPreview()
                }
            }
        }
    }
    override fun onResume()
    {
        codeScanner?.startPreview()
        super.onResume()

    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
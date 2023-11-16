package com.property.propertyagent.agent_panel.ui.main.sidemenu.qrscanner

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.listeners.FragmentTransInterface
import kotlinx.android.synthetic.main.fragment_qrscanner.*

class QrScannerFragment : BaseFragment() {
    private var codeScanner: CodeScanner? = null

    private lateinit var fragmentTransInterface: FragmentTransInterface

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_qrscanner, container, false)
    }

    override fun initData() {
        methodWithPermissions()

        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.qr_reader))
    }

    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
            if (result.allGranted()) {
                val activity = requireActivity()
                codeScanner = CodeScanner(activity, scanner_view)
                codeScanner!!.decodeCallback = DecodeCallback {
                    activity.runOnUiThread {
                        Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                    }
                }
                scanner_view.setOnClickListener {
                    codeScanner!!.startPreview()
                }
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

    override fun onResume() {
        codeScanner?.startPreview()
        super.onResume()

    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}
package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.ownerdetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.widget.Toast
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.PropertyDetailsPage1Activity
import com.property.propertyagent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_owner_detailed_.*
import kotlinx.android.synthetic.main.toolbar_main.*

class OwnerDetailedActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_owner_detailed_
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fragment_ownerdetailed_featuresTv.paintFlags =
            fragment_ownerdetailed_featuresTv.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        fragment_ownerdetailed_editdetailsBtn.setOnClickListener {
            startActivity(Intent(this, PropertyDetailsPage1Activity::class.java))
        }

        fragment_ownerdetailed_CallBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
                if (result.allGranted()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:<number>")
                    startActivity(intent)
                }
            }
        }

        fragment_ownerdetailed_WhtasppBtn.setOnClickListener {
            val pm: PackageManager = packageManager
            val url = "https://api.whatsapp.com/send?phone=$0000000000"
            try {
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
                val waIntent = Intent(Intent.ACTION_SEND)
                waIntent.type = "text/plain"
                waIntent.setPackage("com.whatsapp")
                waIntent.data = Uri.parse(url)
                waIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT")
                startActivity(waIntent)

            } catch (e: Exception) {
                Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show()
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
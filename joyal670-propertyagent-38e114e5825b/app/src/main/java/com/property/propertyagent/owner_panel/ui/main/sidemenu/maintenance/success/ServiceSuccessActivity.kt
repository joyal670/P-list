package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.success

import android.app.Activity
import android.content.Intent
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_service_success_.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class ServiceSuccessActivity : BaseActivity()
{
    var params = ""
    override val layoutId: Int
        get() = R.layout.activity_service_success_
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        params = intent.getStringExtra("params")!!

        setSupportActionBar(owner_toolbar)
        supportActionBar?.title = getString(R.string.success)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupUI() {
        tvSuccessfullText.text = params
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        intent.putExtra("statuscode", "success")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun onClicks() {
        btnViewStatus.setOnClickListener {
            val intent = Intent()
            intent.putExtra("statuscode", "success")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}
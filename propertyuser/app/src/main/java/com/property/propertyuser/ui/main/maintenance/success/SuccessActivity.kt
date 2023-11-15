package com.property.propertyuser.ui.main.maintenance.success

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityStatusDetailsBinding
import com.property.propertyuser.databinding.ActivitySuccessBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.maintenance.MaintenanceActivity
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() , ActivityListener {
    override fun getViewBinging(): ActivitySuccessBinding = ActivitySuccessBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_success
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle("Success")
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        btnViewStatus.setOnClickListener {
            /*val intent = Intent(this,MaintenanceActivity::class.java);
            intent.putExtra("statuscode", "success")
            startActivity(intent);*/
            val intent = Intent()
            intent.putExtra("statuscode", "success")
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("statuscode", "success")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}
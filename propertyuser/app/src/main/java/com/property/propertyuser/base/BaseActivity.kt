package com.property.propertyuser.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import com.property.propertyuser.R
import com.property.propertyuser.dialogs.CustomProgressDialog
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*


abstract class BaseActivity <DB : ViewBinding>: AppCompatActivity() {
    abstract val layoutId: Int
    abstract val setToolbar: Boolean
    abstract val hideStatusBar: Boolean
    abstract fun fragmentLaunch()
    abstract fun initData()
    abstract fun setupUI()
    abstract fun setupViewModel()
    abstract fun setupObserver()
    abstract fun onClicks()
    abstract fun getViewBinging(): DB
    lateinit var binding: DB
    protected lateinit var customProgressDialog: CustomProgressDialog
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinging()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (hideStatusBar) statusBarHide()
        //setContentView(layoutId)
        setContentView(binding.root)
        setupViewModel()
        initData()
        if (setToolbar) initializeToolbar("")
        customProgressDialog=CustomProgressDialog()
        onClicks()
        setupObserver()
        fragmentLaunch()
    }

    private fun statusBarHide() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun initializeToolbar(title:String) {
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            tvToolbarTitle.text=title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    /*fun dismissLoader() {
        customProgressDialog.dialog.dismiss()
    }

    fun showLoader() {
        customProgressDialog.show(this)

    }*/
    fun showLoader() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.show()
    }

    fun dismissLoader() {
        dialog!!.dismiss()

    }
}
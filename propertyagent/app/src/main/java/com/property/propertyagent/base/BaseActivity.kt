package com.property.propertyagent.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.property.propertyagent.R
import kotlinx.android.synthetic.main.toolbar_main.*

abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    private var dialog: AlertDialog? = null
    private var dialogOwner: AlertDialog? = null
    abstract val setToolbar: Boolean
    abstract val hideStatusBar: Boolean
    abstract fun fragmentLaunch()
    abstract fun initData()
    abstract fun setupUI()
    abstract fun setupViewModel()
    abstract fun setupObserver()
    abstract fun onClicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hideStatusBar) statusBarHide()
        setContentView(layoutId)
        setupViewModel()
        if (setToolbar) initializeToolbar()
        initData()
        onClicks()
        setupObserver()
        setupUI()
        adjustFontScale(resources.configuration)
    }

    private fun statusBarHide() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initializeToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    open fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1.1) {
            configuration.fontScale = 1.1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
        if (configuration.fontScale < 1.1) {
            configuration.fontScale = 1.1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    fun showProgress() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.show()
    }

    fun dismissProgress() {
        dialog!!.dismiss()

    }

    fun showProgressOwner() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog_owner, null)
        builder.setView(dialogView)
        builder.setCancelable(true)
        dialogOwner = builder.create()
        dialogOwner!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogOwner!!.show()
    }

    fun dismissProgressOwner() {
        dialogOwner!!.dismiss()

    }
}
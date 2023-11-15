package com.proteinium.proteiniumdietapp.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.proteinium.proteiniumdietapp.dialogs.CustomProgressDialog
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.zeugmasolutions.localehelper.LocaleHelper
import kotlinx.android.synthetic.main.toolbar_main.*


abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    abstract val setToolbar: Boolean
    abstract val hideStatusBar: Boolean
    abstract fun fragmentLaunch()
    abstract fun initData()
    abstract fun setupUI()
    abstract fun setupViewModel()
    abstract fun setupObserver()
    abstract fun onClicks()

    protected lateinit var customProgress: CustomProgressDialog




    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("TAG", "onCreate: " )
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration)
        if (hideStatusBar) statusBarHide()
        setContentView(layoutId)
        setupViewModel()
        initData()
        if (setToolbar) initializeToolbar("")
        customProgress = CustomProgressDialog()
        setupObserver()
        onClicks()
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
//            setSupportActionBar(toolbar)
//            supportActionBar!!.setDisplayShowTitleEnabled(false)
//            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward)
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            tvToolbarTitle.text=title

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

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        val context = super.createConfigurationContext(overrideConfiguration)
        return LocaleHelper.onAttach(context)
    }



    open fun adjustFontScale(configuration: Configuration)
    {
        Log.e("font", "adjustFontScale: ${configuration.fontScale}")
        if (configuration.fontScale > 1.1)
        {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
        if(configuration.fontScale<1.1)
        {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }
    fun dismissLoader() {
        customProgress.dialog.dismiss()
    }

    fun showLoader() {
        customProgress.show(this)
    }
}
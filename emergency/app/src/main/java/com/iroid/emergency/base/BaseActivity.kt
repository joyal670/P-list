package com.iroid.emergency.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.iroid.emergency.R
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : ViewModel, B : ViewBinding> : AppCompatActivity() {
    abstract val setToolbar: Boolean
    abstract val hideStatusBar: Boolean
    lateinit var binding: B
    lateinit var viewModel: VM
    private var dialog: AlertDialog? = null
    abstract fun setOnClick()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        if (hideStatusBar) statusBarHide()
        setContentView(binding.root)
        setOnClick()
        if (setToolbar) initializeToolbar()
        initViews()
        adjustFontScale(resources.configuration)

    }

    open fun adjustFontScale(configuration: Configuration) {
        Log.e("font", "adjustFontScale: ${configuration.fontScale}")
        if (configuration.fontScale > 1.1) {
            configuration.fontScale = 1.0f
            val metrics = resources.displayMetrics
            val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
        if (configuration.fontScale < 1.1) {
            configuration.fontScale = 1.0f
            val metrics = resources.displayMetrics
            val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun initViews()
    abstract fun getViewBinding(): B

    private fun initializeToolbar() {
//        setSupportActionBar()
//        setUpIconVisibility(true)
    }


    private fun statusBarHide() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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


}

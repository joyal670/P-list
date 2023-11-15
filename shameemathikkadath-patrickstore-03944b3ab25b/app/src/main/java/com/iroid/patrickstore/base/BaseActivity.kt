package com.iroid.patrickstore.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.github.jorgecastillo.FillableLoaderBuilder
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.baseinterface.ActionBarView
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : ViewModel, B : ViewBinding> : AppCompatActivity(), ActionBarView {


    abstract val layoutId: Int
    abstract val setToolbar: Boolean
    abstract val hideStatusBar: Boolean
    private var dialog: AlertDialog? = null
    lateinit var binding: B
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        if (hideStatusBar) statusBarHide()
        setContentView(binding.root)
        if (setToolbar) initializeToolbar()
        initViews()

    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun initViews()
    abstract fun getViewBinding(): B

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        setUpIconVisibility(true)
    }

    override fun setUpIconVisibility(visible: Boolean) {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(visible)
    }

    override fun setTitle(titleKey: String) {
        val actionBar = supportActionBar
//        if (actionBar != null) {
//            binding.tvToolbarTitle?.text = titleKey
//        }
    }

    override fun setSettingsIconVisibility(visibility: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
//            ic_toolbar_setting?.visibility = if (visibility) VISIBLE else GONE
        }
    }

    override fun setRefreshVisibility(visibility: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
//            ic_toolbar_refresh?.visibility = if (visibility) VISIBLE else GONE
        }
    }


    override fun statusBarHide() {
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
        val loaderBuilder = FillableLoaderBuilder()
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

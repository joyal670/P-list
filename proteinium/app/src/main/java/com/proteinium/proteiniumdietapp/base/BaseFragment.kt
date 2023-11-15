package com.proteinium.proteiniumdietapp.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.proteinium.proteiniumdietapp.dialogs.CustomProgressDialog

abstract class BaseFragment : Fragment() {

    var appCompatActivity: AppCompatActivity? = null
    protected lateinit var customProgressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("oncreate base", "iii")
        appCompatActivity = activity as AppCompatActivity
        customProgressDialog = CustomProgressDialog()
    }

    abstract fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    abstract fun initData()
    abstract fun setupUI()
    abstract fun setupViewModel()
    abstract fun setupObserver()
    abstract fun onClicks()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
        setupObserver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupUI()
        onClicks()
        adjustFontScale(resources.configuration)
    }

    open fun adjustFontScale(configuration: Configuration) {
        Log.e("font", "adjustFontScale: ${configuration.fontScale}")
        if (configuration.fontScale > 1.1) {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = context?.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context?.resources?.updateConfiguration(configuration, metrics)
        }
        if (configuration.fontScale < 1.1) {
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = context?.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context?.resources?.updateConfiguration(configuration, metrics)
        }
    }

    fun dismissLoader() {
        customProgressDialog.dialog.dismiss()
    }

    fun showLoader(context: Context? = null) {
        customProgressDialog.show(requireContext())
    }

}
package com.ncomfortsagent.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ncomfortsagent.R

abstract class BaseFragment : Fragment() {

    var appCompatActivity: AppCompatActivity? = null
    private var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActivity = activity as AppCompatActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        onClicks()
        setupUI()
        adjustFontScale(resources.configuration)
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        setupViewModel()
        setupObserver()
    }

    open fun adjustFontScale(configuration: Configuration)
    {
        if (configuration.fontScale > 1.1) {
            configuration.fontScale = 1.1f
            val metrics = resources.displayMetrics
            val wm = context?.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context?.resources?.updateConfiguration(configuration, metrics)
        }
        if(configuration.fontScale<1.1){
            configuration.fontScale = 1.1f
            val metrics = resources.displayMetrics
            val wm = context?.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context?.resources?.updateConfiguration(configuration, metrics)
        }
    }
    fun showProgress() {
        val builder = activity?.let { AlertDialog.Builder(it) }
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder!!.setView(dialogView)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.show()
    }

    fun dismissProgress() {
        dialog!!.dismiss()

    }
}
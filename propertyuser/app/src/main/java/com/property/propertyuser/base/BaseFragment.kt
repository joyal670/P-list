package com.property.propertyuser.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.property.propertyuser.dialogs.CustomProgressDialog

abstract class BaseFragment : Fragment() {

    var appCompatActivity: AppCompatActivity? = null
    protected lateinit var customProgressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
    fun dismissLoader() {
        customProgressDialog.dialog.dismiss()
    }

    fun showLoader() {
        customProgressDialog.show(requireContext())
    }

}
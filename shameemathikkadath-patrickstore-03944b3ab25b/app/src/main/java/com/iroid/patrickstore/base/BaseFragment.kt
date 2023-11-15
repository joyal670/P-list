package com.iroid.patrickstore.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.iroid.patrickstore.R
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    lateinit var binding: B
    lateinit var viewModel: VM

    abstract fun initViews()
    abstract fun setUpObserver()
    abstract fun setOnClick()

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("123", "onCreateView: "+"123" )
        binding = getViewBinding()
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        initViews()
        setUpObserver()
        setOnClick()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun getViewBinding(): B

    fun showProgress() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.show()
    }

    fun dismissProgress() {
        dialog!!.dismiss()

    }
}

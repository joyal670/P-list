package com.iroid.emergency.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.iroid.emergency.start_up.utils.Loader
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    lateinit var binding: B
    lateinit var viewModel: VM
    protected lateinit var loading: Loader
    abstract fun initViews()
    abstract fun setUpObserver()
    abstract fun setOnClick()
    var view =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("#333333","onCreateView")
        binding = getViewBinding()
        loading = Loader(requireContext())
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        initViews()
        setOnClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("#44444444","onViewStateRestored")
    }


    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun getViewBinding(): B


    open fun getLoadingWidth(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.widthPixels
    }

    open fun getLoadingHeight(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.heightPixels
    }


    fun dismissProgress() {
        loading.dismiss()

    }

    fun showProgress() {
        loading.show()
        loading.window?.setLayout(getLoadingWidth(), getLoadingHeight())
        loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

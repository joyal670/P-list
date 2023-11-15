package com.iroid.healthdomain.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.iroid.healthdomain.data.network.RemoteDataSource
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding, R : BaseRepository> : Fragment() {


    protected lateinit var binding: DB
    protected lateinit var viewModel: VM
    protected val remoteDataSource: RemoteDataSource by lazy { RemoteDataSource() }
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var loading: Loader

    companion object {
        private const val TAG = "BaseFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //super.onCreateView(inflater, container, savedInstanceState)


        binding = getFragmentBinding(inflater, container)
        loading = Loader(requireContext())
        userPreferences = UserPreferences(requireContext())
        val factory = ViewModelFactory(getFragmentRepository(), requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch { userPreferences.authToken.first() }

        return binding.root
    }


    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): DB
    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentRepository(): R


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) showLoader()
            else dismissLoader()
        })
    }

    fun dismissLoader() {
        loading.dismiss()
    }

    fun showLoader() {
        loading.show()
        loading.window?.setLayout(getLoadingWidth(), getLoadingHeight())
        loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    open fun getLoadingWidth(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.widthPixels
    }

    open fun getLoadingHeight(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.heightPixels
    }


}
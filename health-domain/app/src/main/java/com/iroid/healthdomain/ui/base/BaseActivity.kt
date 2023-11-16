package com.iroid.healthdomain.ui.base

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.RemoteDataSource
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.home.fit.FitRequestCode


abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding,R : BaseRepository> : AppCompatActivity() {

    protected lateinit var userPreferences: UserPreferences
    protected val remoteDataSource: RemoteDataSource by lazy { RemoteDataSource() }
    protected lateinit var viewModel: VM
    protected lateinit var loading: Loader
    private val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED)
            .build()



    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }



    abstract fun getFragmentRepository(): R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(context = applicationContext)
        setContentView(binding.root)
        loading = Loader(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val factory = ViewModelFactory(getFragmentRepository(), applicationContext as Application)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        viewModel.isLoading.observe(this, Observer {
            if (it) showLoader()
            else dismissLoader()
        })
        init()

    }
    fun dismissLoader() {
        loading.dismiss()
    }

    fun showLoader() {
        loading.show()
        loading.window?.setLayout(getLoadingWidth(), getLoadingHeight())
        loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    @LayoutRes
    abstract fun getLayoutRes(): Int
    abstract fun getViewModel(): Class<VM>

    abstract fun init()


    open fun getLoadingWidth(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.widthPixels
    }

    open fun getLoadingHeight(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.heightPixels
    }
}



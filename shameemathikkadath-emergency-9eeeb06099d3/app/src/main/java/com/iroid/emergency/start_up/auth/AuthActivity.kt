package com.iroid.emergency.start_up.auth

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseActivity
import com.iroid.emergency.databinding.ActivityAuthBinding
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_home.*

class AuthActivity : BaseActivity<AuthViewModal, ActivityAuthBinding>() {
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun initViews() {


    }

    override fun setOnClick() {

    }
    override fun onBackPressed() {

        val navigationController = nav_host_fragment.findNavController()

        when {
            navigationController.currentDestination!!.id == R.id.signUpFragment -> {
                finish()
            }
            navigationController.currentDestination?.id == R.id.action_signUpFragment_to_otpFragment2 -> {
                navigationController.navigate(R.id.action_otpFragment_to_signUpFragment)
            }
            navigationController.currentDestination?.id == R.id.otpFragment -> {
                navigationController.navigate(R.id.signUpFragment)
            }
            else -> {
                super.onBackPressed()
            }
        }

    }
    override fun getViewBinding(): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(layoutInflater)
    }
}



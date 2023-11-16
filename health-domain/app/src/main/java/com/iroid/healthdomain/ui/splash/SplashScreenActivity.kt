package com.iroid.healthdomain.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActivitySplashScreenBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.home.HomeActivity
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.home.my_health.MyHealthViewModel
import com.iroid.healthdomain.ui.login.LoginActivity
import com.iroid.healthdomain.ui.preference.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : BaseActivity<MyHealthViewModel, ActivitySplashScreenBinding, MyHealthRepository>() {

    private val TAG = "SplashScreenActivity"

    private val REQ_CODE_LOCATION: Int = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        val actionBar = supportActionBar
        actionBar?.hide()
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }


    private fun getUser() {
        if(AppPreferences.isLogin){
            gotoHome()
        }else{
            gotoLogin()
        }
    }

    private fun gotoHome() {
        val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 2000
    }

    override fun getLayoutRes(): Int = R.layout.activity_splash_screen
    override fun getViewModel(): Class<MyHealthViewModel> {
        return MyHealthViewModel::class.java
    }


    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.READ_CONTACTS,
                ),
                REQ_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size!=0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                getUser()
            } else {
                requestPermission()

            }

        }


    }
    override fun init() {
        if (hasPermission()) {
            lifecycleScope.launch {
                getUser()

            }
        } else {
            requestPermission()
        }

    }

    override fun getFragmentRepository(): MyHealthRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return MyHealthRepository(api, userPreferences)
    }
}

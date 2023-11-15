package com.iroid.jeetmeet.start_up.auth.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityAuthBinding
import com.iroid.jeetmeet.listeners.ActivityListener
import com.iroid.jeetmeet.start_up.auth.fragment.LoginTypeFragment
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.ui.main.student_panel.home.activity.StudentDashboardActivity
import com.iroid.jeetmeet.utils.AppPreferences.prefIsLogin
import com.iroid.jeetmeet.utils.AppPreferences.prefLoginType
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.DataStoreUserPreferences
import com.iroid.jeetmeet.utils.replaceFragment

class AuthActivity : BaseActivity<ActivityAuthBinding>(), ActivityListener {
    private lateinit var userPreferences: DataStoreUserPreferences

    override val layoutId: Int
        get() = R.layout.activity_auth
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)

    override fun initData() {
        fragmentLaunch()
    }

    override fun fragmentLaunch() {
        replaceFragment(fragment = LoginTypeFragment())
    }

    override fun setupUI() {
        userPreferences = DataStoreUserPreferences(this)
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    override fun onResume() {
        super.onResume()

       /* lifecycleScope.launch {
            val isLogin = userPreferences.getBooleanValue(this@AuthActivity,Constants.IS_LOGIN,false)
            val type = userPreferences.getStringValue(this@AuthActivity,Constants.STUDENT,"")
            if(isLogin)
            {
                if(type == Constants.STUDENT)
                {
                    val intent = Intent(this@AuthActivity, StudentDashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                if(type == Constants.PARENT)
                {
                    val intent = Intent(this@AuthActivity, ParentDashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }*/
        if (prefIsLogin) {
            if (prefLoginType == Constants.STUDENT) {
                val intent = Intent(this@AuthActivity, StudentDashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            if(prefLoginType == Constants.PARENT)
            {
                val intent = Intent(this@AuthActivity, ParentDashboardActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

}
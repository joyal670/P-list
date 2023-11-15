package com.iroid.healthdomain.ui.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActivityLoginBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding,LoginRepository>() {
    private var toolbarTitle: TextView? = null
    private var actionBar: ActionBar? = null

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object{
        private const val TAG = "LoginActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initActionBar()
//        initViews()

//        binding.toolbar.title = ""
//        setSupportActionBar(binding.toolbar)


        navController = Navigation.findNavController(this, R.id.nav_host_start_fragment)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.PhoneNumberFragment -> {
                    binding.toolbarTitle.text = resources.getString(R.string.title_activity_login)
                }
                R.id.OTPFragment ->{
                    binding.toolbarTitle.text = resources.getString(R.string.otp_fragment_label)
                }
            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }


    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java


    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar
    }

    private fun initViews() {
        toolbarTitle = findViewById(R.id.toolbar_title)
        if (actionBar != null) {
            //toolbarTitle.setText(actionBar.getTitle());
            actionBar!!.setDisplayShowTitleEnabled(false)
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setDisplayShowHomeEnabled(true)
        }
    }
    override fun init() {

    }

    override fun getFragmentRepository(): LoginRepository {
        return LoginRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }
}
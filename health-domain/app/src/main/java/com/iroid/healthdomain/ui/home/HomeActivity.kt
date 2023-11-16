package com.iroid.healthdomain.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.awesomedialog.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.navigation.NavigationView
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActivityHomeBinding
import com.iroid.healthdomain.databinding.HomeDrawerHeaderBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.cms.privacy_policy.PrivacyPolicyActivity
import com.iroid.healthdomain.ui.cms.terms_conditions.TermsConditionActivity
import com.iroid.healthdomain.ui.home.fit.FitRequestCode
import com.iroid.healthdomain.ui.home.fit.TAG
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.mainActivity.HomeViewModel
import com.iroid.healthdomain.ui.home.mainActivity.MainActivity
import com.iroid.healthdomain.ui.home.notification.NotificationActivity
import com.iroid.healthdomain.ui.login.LoginActivity
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.utils.MyContacts
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log


class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding,HomeRepository>() ,NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
        .build()
    val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    companion object {
        private const val TAG = "HomeActivity"
        lateinit var homeDrawerHeaderBinding: HomeDrawerHeaderBinding
    }


    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java



    override fun onResume() {
        super.onResume()
       if(AppPreferences.isLoaded){
           refreshCurrentFragment()
           AppPreferences.isLoaded=false
       }
        userPreferences.isNotification.asLiveData().observe(this, Observer {
            if (it==true){
                binding.appBar.viewNotificationBadge.visibility=View.VISIBLE
            }else{
                binding.appBar.viewNotificationBadge.visibility=View.GONE
            }
        })
    }

    fun setDrawerHeader(imageUrl: String, name: String) {
        homeDrawerHeaderBinding.employeeName.text = name
        Glide.with(homeDrawerHeaderBinding.profileImage.context).load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .placeholder(R.drawable.placehold)
            .into(homeDrawerHeaderBinding.profileImage)


    }
    private fun refreshCurrentFragment(){
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
                navController, binding.drawerLayout
        )
    }

    fun onFabClick(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun onNotificationClick(view: View){
        val intent = Intent(applicationContext, NotificationActivity::class.java)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout ->{
                openLogoutDialog()
               /* val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()*/
            }
            R.id.menu_one ->{
                 val intent = Intent(this, TermsConditionActivity::class.java)
                 startActivity(intent)

            }
            R.id.menu_two ->{
                val intent = Intent(this, PrivacyPolicyActivity::class.java)
                startActivity(intent)

            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openLogoutDialog() {
        AwesomeDialog.build(this)
            .title("Logout")
            .body("Are you sure you want to logout?")
            .icon(R.drawable.ic_logout)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.signOut()
                GlobalScope.launch {
                    userPreferences.clear()
                }
                AppPreferences.cleareSharedPreference()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            .onNegative("No", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                Log.d("TAG", "negative ")
            }
    }

    override fun init() {
        setSupportActionBar(binding.appBar.toolbar)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            checkPermissionsAndRun(FitRequestCode.SUBSCRIBE)
        }
        binding.appBar.bottomNavBar.background = null
        binding.appBar.bottomNavBar.menu.getItem(1).isEnabled = false

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_home)
        binding.appBar.bottomNavBar.itemIconTintList = null
        NavigationUI.setupWithNavController(binding.homeNavigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.appBar.bottomNavBar, navController)

        //Drawer header layout setting
        homeDrawerHeaderBinding = HomeDrawerHeaderBinding.inflate(layoutInflater, binding.homeNavigationView, false)
        binding.homeNavigationView.addHeaderView(homeDrawerHeaderBinding.root)
        binding.homeNavigationView.setNavigationItemSelectedListener(this)



        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.myHealthFragment -> {
                    binding.appBar.tabTitle.text = "Home"
                    binding.appBar.appBarLayout.elevation = 6f
                }
                R.id.profileFragment -> {
                    binding.appBar.tabTitle.text = "Profile"
                    binding.appBar.appBarLayout.elevation = 6f
                }
            }
        }




        val intent  = Intent(this, MyContacts::class.java)
        startService(intent)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkPermissionsAndRun(fitActionRequestCode: FitRequestCode) {

        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)

        } else {
            requestRuntimePermissions(fitActionRequestCode)

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun permissionApproved(): Boolean {
        val approved = if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACTIVITY_RECOGNITION)

        } else {
            true
        }
        return approved
    }

    private fun fitSignIn(requestCode: FitRequestCode) {
        if (oAuthPermissionsApproved()) {
            performActionForRequestCode(requestCode)
            //findFitnessDataSources()
        } else {
            requestCode.let {
                GoogleSignIn.requestPermissions(
                    this,
                    requestCode.ordinal,
                    getGoogleAccount(), fitnessOptions)
            }
        }
    }


    private fun oAuthPermissionsApproved() = GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)


    private fun performActionForRequestCode(requestCode: FitRequestCode) = when (requestCode) {
        FitRequestCode.SUBSCRIBE -> subscribe()
        FitRequestCode.READ_DATA -> {

        }
        FitRequestCode.UNSUBSCRIBE -> {
        }
    }

    private fun subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(this, getGoogleAccount())
            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Successfully subscribed!")
                    performActionForRequestCode(FitRequestCode.READ_DATA)

                } else {
                    Log.w(TAG, "There was a problem subscribing.", task.exception)
                }
            }
    }

    private fun requestRuntimePermissions(requestCode: FitRequestCode) {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACTIVITY_RECOGNITION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        requestCode.let {
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.")
                //showToast("permission_rationale")
            } else {
                Log.i(TAG, "Requesting permission")
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    requestCode.ordinal)
            }
        }
    }
    override fun getFragmentRepository(): HomeRepository {
        return HomeRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }


}

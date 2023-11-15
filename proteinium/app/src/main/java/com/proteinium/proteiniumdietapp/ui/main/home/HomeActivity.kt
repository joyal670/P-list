package com.proteinium.proteiniumdietapp.ui.main.home

import android.content.Intent
import android.content.IntentSender
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isCalendar
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.EnumFromPage
import com.proteinium.proteiniumdietapp.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_main.*


class HomeActivity : BaseActivity(), ActivityListener {
    private lateinit var navController: NavController
    private lateinit var mAppUpdateManger:AppUpdateManager
    private val RC_APP_UPDATE=100

    private val dispatcher by lazy { onBackPressedDispatcher }
    lateinit var callback: OnBackPressedCallback
    override val layoutId: Int
        get() = R.layout.activity_home
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }


    override fun initData() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_calender,
                R.id.navigation_profile,
                R.id.navigation_more
            )
        )
        val bundle = intent.extras
        if(bundle!=null){
           if(bundle!!.getString("type")=="rate_food"){
               navController.navigate(  R.id.navigation_calender)
               isCalendar=true
           }
        }


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if(intent.getStringExtra(Constants.TYPE)==EnumFromPage.CALENDER.name){
            navController.navigate(  R.id.navigation_calender)
        }
        callback = dispatcher.addCallback {
            showConfirmDialogue()
        }

        mAppUpdateManger=AppUpdateManagerFactory.create(this)
        mAppUpdateManger.appUpdateInfo.addOnSuccessListener {
                appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    mAppUpdateManger.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this,RC_APP_UPDATE)
                }catch (e:IntentSender.SendIntentException){

                }

            }
        }
        
    }

    override fun onResume() {
        super.onResume()
        mAppUpdateManger.appUpdateInfo.addOnSuccessListener {
                appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    mAppUpdateManger.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this,RC_APP_UPDATE)
                }catch (e:IntentSender.SendIntentException){

                }

            }
        }
    }

    private fun showConfirmDialogue() {
        val snackbar =
            Snackbar.make(nav_view, getString(R.string.exit_command), Snackbar.LENGTH_LONG)
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
        snackbar.setAction(getString(R.string.yes)) {
            onConfirm()
        }.apply {
            anchorView = nav_view
        }.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_APP_UPDATE && resultCode== RESULT_OK){

        }
    }



    private fun onConfirm() {
        callback.isEnabled = false
        dispatcher.onBackPressed()
        finishAffinity()
    }





    override fun setupUI() {

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

    override fun setTitle(title: String, size: Float, fontFamily: Int, textAllCaps: Boolean) {
        tvToolbarTitle.text = title
        tvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tvToolbarTitle.typeface = ResourcesCompat.getFont(this, fontFamily)
        tvToolbarTitle.isAllCaps = textAllCaps

    }



    override fun setBackButton(backEnabled: Boolean) {
        if(AppPreferences.chooseLanguage==Constants.ENGLISH_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(backEnabled)
    }

    override fun hideToolbar(hideToolbar: Boolean) {
        if (hideToolbar) {
            home_toolbar.visibility = View.GONE
        } else {
            home_toolbar.visibility = View.VISIBLE
        }
    }


}
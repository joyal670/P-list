package com.property.propertyagent.owner_panel.ui.main.home.home.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.listeners.MenueListener
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity.AddPropertyMainPageCopy
import com.property.propertyagent.owner_panel.ui.main.sidemenu.change_password.OwnerChangePasswordActivity
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.MaintenanceActivity
import com.property.propertyagent.owner_panel.ui.main.sidemenu.ownerservice.OwnerServiceActivity
import com.property.propertyagent.owner_panel.ui.main.sidemenu.profile.activity.OwnerProfileActivity
import com.property.propertyagent.owner_panel.ui.main.sidemenu.reports.ReportsActivity
import com.property.propertyagent.start_up.auth.activity.AuthActivity
import com.property.propertyagent.start_up.auth.viewmodel.LoginViewModel
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.user_email
import com.property.propertyagent.utils.AppPreferences.user_lang
import com.property.propertyagent.utils.AppPreferences.user_name
import com.property.propertyagent.utils.AppPreferences.user_profileImg
import com.property.propertyagent.utils.CommonUtils.Companion.changeLanguageAwareContext
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.shameem.projectstructure.listeners.ActivityListener
import com.property.propertyagent.utils.isConnected
import com.property.propertyagent.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_home_owner.*
import kotlinx.android.synthetic.main.app_bar_main_owner.*
import kotlinx.android.synthetic.main.custom_drawercustom_drawer_owner.*
import render.animations.Fade
import render.animations.Render
import java.util.*
import kotlin.system.exitProcess

class HomeOwnerActivity : BaseActivity(), ActivityListener, MenueListener,
    FragmentTransInterface {

    private lateinit var logoutViewModal: LoginViewModel
    private var doubleBackToExitPressedOnce = false

    override val layoutId: Int
        get() = R.layout.activity_home_owner
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

        /* toolbar */
        setSupportActionBar(ownertoolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        /* navigation drawer */
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout_owner,
            ownertoolbar,
            R.string.open,
            R.string.close
        )
        drawer_layout_owner.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.isDrawerSlideAnimationEnabled = true
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_owner)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawer_layout_owner.isDrawerVisible(GravityCompat.START)) {
                drawer_layout_owner.closeDrawer(GravityCompat.START)
            } else {
                val render = Render(this)
                render.setAnimation(Fade().InLeft(homeOwnerHeader))
                render.setDuration(500)
                render.start()
                drawer_layout_owner.openDrawer(GravityCompat.START)
            }
        }

        /* bottom navigation */
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view_owner)
        val navController = findNavController(R.id.nav_host_fragment_owner)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_owner,
                R.id.navigation_property_owner,
                R.id.navigation_payment_owner,
                R.id.navigation_notifications_owner
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun initData() {
        fragmentLaunch()
        refreshProfile(user_profileImg, user_name, user_email)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        logoutViewModal = LoginViewModel()
    }

    override fun setupObserver() {

        /* logout api */
        logoutViewModal.logout.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    /* clear preference */
                    AppPreferences.cleareSharedPreference()

                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
            }
        })
    }

    override fun onClicks() {

        /* home */
        tvHomeOwner.setOnClickListener {
            drawer_layout_owner.closeDrawers()
            val intent = Intent(this, HomeOwnerActivity::class.java)
            startActivity(intent)
        }

        /* profile */
        tvProfileOwner.setOnClickListener {
            val intent = Intent(this, OwnerProfileActivity::class.java)
            startActivity(intent)
        }

        /* maintenance */
        tvServiceRequestOwner.setOnClickListener {
            val intent = Intent(this, OwnerServiceActivity::class.java)
            startActivity(intent)
        }

        /* service */
        tvServicesOwner.setOnClickListener {
            val intent = Intent(this, MaintenanceActivity::class.java)
            startActivity(intent)
        }

        /* report */
        tvReportsOwner.setOnClickListener {
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }

        /* change password */
        tvChangePasswordOwner.setOnClickListener {
            val intent = Intent(this, OwnerChangePasswordActivity::class.java)
            startActivity(intent)
        }

        /* logout */
        tvLogoutOwner.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.logout_layout_owner)

            val yesBtn = dialog.findViewById(R.id.logoutYesBtnOwner) as MaterialButton
            val noBtn = dialog.findViewById(R.id.logoutNoBtnOwner) as MaterialButton

            yesBtn.setOnClickListener {
                logoutViewModal.logout()
            }

            noBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams

        }
    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    override fun menueHide(boolean: Boolean) {

    }

    /* option menu setup */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_toolbar_menu_owner, menu)

        val one =
            menu?.findItem(R.id.customtoolbar_addProperty)?.actionView?.findViewById<ImageView>(R.id.menu_Img)
        val two =
            menu?.findItem(R.id.customtoolbar_addProperty)?.actionView?.findViewById<TextView>(R.id.menu_Text)
        one?.setOnClickListener {
            val intent = Intent(this, AddPropertyMainPageCopy::class.java)
            startActivity(intent)
        }
        two?.setOnClickListener {
            val intent = Intent(this, AddPropertyMainPageCopy::class.java)
            startActivity(intent)
        }

        return super.onCreateOptionsMenu(menu)
    }

    /* option menu clicks */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_addProperty -> {
            val intent = Intent(this, AddPropertyMainPageCopy::class.java)
            startActivity(intent)
            true
        }
        R.id.customtoolbar_search -> {
            true
        }

        R.id.customtoolbar_translate -> {
            showSelectLanguageDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showSelectLanguageDialog() {
        val items = arrayOf(getString(R.string.english), getString(R.string.arabic))
        val materialBuilder = MaterialAlertDialogBuilder(this, R.style.custom_material_theme)
        materialBuilder.setTitle(getString(R.string.select_lang))
        materialBuilder.setItems(items) { dialog, which ->
            if (which == 0) {
                changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                user_lang = Constants.ENGLISH_LAG
                startActivity(Intent(this, HomeOwnerActivity::class.java))
                overridePendingTransition(0, 0)

            }
            if (which == 1) {
                changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                user_lang = Constants.ARABIC_LAG
                startActivity(Intent(this, HomeOwnerActivity::class.java))
                overridePendingTransition(0, 0)

            }
        }
        materialBuilder.show()
    }

    /* handle back press */
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } else {

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(R.string.please_click_back_to_exit),
                Toast.LENGTH_SHORT
            ).show()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    doubleBackToExitPressedOnce = false
                }
            }, 3000)

            if (drawer_layout_owner.isDrawerOpen(GravityCompat.START)) {
                drawer_layout_owner.closeDrawer(GravityCompat.START)
            }
        }
    }

    /* setting toolbar title */
    override fun setTitleFromFragment(title: String) {
        ownertoolbartvToolbarTitle1.text = title
    }

    /* refresh profile */
    private fun refreshProfile(pic: String, name: String, email: String) {
        tvProfileNameOwner.text = name
        tvProfileEmailOwner.text = email
        Glide.with(this)
            .load(pic)
            .placeholder(R.drawable.ic_static_user)
            .into(ivProfileOwner)
    }

    override fun onResume() {
        super.onResume()
        refreshProfile(user_profileImg, user_name, user_email)
    }
}
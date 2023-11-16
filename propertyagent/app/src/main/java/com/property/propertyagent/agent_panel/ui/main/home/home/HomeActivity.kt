package com.property.propertyagent.agent_panel.ui.main.home.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.MenueListener
import com.property.propertyagent.start_up.auth.activity.AuthActivity
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.isProfileChanged
import com.property.propertyagent.utils.AppPreferences.user_email
import com.property.propertyagent.utils.AppPreferences.user_name
import com.property.propertyagent.utils.AppPreferences.user_profileImg
import com.property.propertyagent.utils.Constants.TYPE
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.custom_drawercustom_drawer.*
import kotlinx.android.synthetic.main.custom_drawercustom_drawer.view.*
import render.animations.Fade
import render.animations.Render

class HomeActivity : BaseActivity(), ActivityListener, MenueListener {
    private var doubleBackToExitPressedOnce = false
    private lateinit var homeViewModel: HomeViewModel

    override val layoutId: Int
        get() = R.layout.activity_home
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        val toggle =
            ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze)
        toggle.isDrawerSlideAnimationEnabled = true
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
                Log.e("CLOSED", "fragmentLaunch: closed")
            } else {
                val render = Render(this)
                render.setAnimation(Fade().InLeft(homeAgentHeader))
                render.setDuration(500)
                render.start()
                Log.e("OPEN", "fragmentLaunch: opened")
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        if (user_profileImg.isNotEmpty()) {
            Glide.with(this)
                .load(user_profileImg)
                .placeholder(R.drawable.ic_static_user)
                .into(drawer_layout.drawerAgent.ivProfile)
        } else {
            Glide.with(this)
                .load(R.drawable.ic_static_user)
                .into(drawer_layout.drawerAgent.ivProfile)
        }

        drawer_layout.drawerAgent.tvProfileName.text = user_name
        drawer_layout.drawerAgent.tvProfileEmail.text = user_email

        // bottom navigation
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_request,
                R.id.navigation_client,
                R.id.navigation_mytask
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun initData() {
        fragmentLaunch()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel()
    }

    override fun setupObserver() {
        homeViewModel.agentLogoutResponse().observe(this) {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    dismissProgress()
                    /* clear preference */
                    AppPreferences.cleareSharedPreference()
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this, getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
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
        }
    }

    override fun onClicks() {

        /* close icon */
        ivClose.setOnClickListener {
            ivClose.animate().rotation(180.0F).start()
            drawer_layout.closeDrawers()
        }

        //language
        tvChangeLang.setOnClickListener {
            showSelectLanguageDialog()
        }

        // home page
        tvHome.setOnClickListener {
            drawer_layout.closeDrawers()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        // profile page
        tvProfile.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.PROFILE.name)
            startActivity(intent)
        }

        // my property page
        tvProperties.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.MYPROFERTIES.name)
            startActivity(intent)
        }

        // my earning page
        tvEarnings.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.MYEARNIBNGS.name)
            startActivity(intent)
        }

        // in hand cash page
        tvHandCash.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.INHANDCASH.name)
            startActivity(intent)
        }

        //  qr page
        tvQrReader.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.QRREADER.name)
            startActivity(intent)
        }

        //  feedback page
        tvFeedback.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.FEEDBACK.name)
            startActivity(intent)
        }

        //  change password page
        tvChangePassword.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.CHANGEPASSWORD.name)
            startActivity(intent)
        }

        // logout page
        tvLogout.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.logout_layout)

            val yesBtn = dialog.findViewById(R.id.logout_Yesbtn) as MaterialButton
            val noBtn = dialog.findViewById(R.id.logout_Nobtn) as MaterialButton

            yesBtn.setOnClickListener {
                homeViewModel.callAgentLogout()
                dialog.dismiss()
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

    private fun showSelectLanguageDialog() {
        val items = arrayOf(getString(R.string.english), getString(R.string.arabic))
        val materialBuilder = MaterialAlertDialogBuilder(this, R.style.custom_material_theme)
        materialBuilder.setTitle(getString(R.string.select_lang))
        materialBuilder.setItems(items) { _, which ->
            if (which == 0) {
                CommonUtils.changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                AppPreferences.user_lang = Constants.ENGLISH_LAG
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(0, 0)
            }
            if (which == 1) {
                CommonUtils.changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                AppPreferences.user_lang = Constants.ARABIC_LAG
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }
        materialBuilder.show()
    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    // option menu setup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custome_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // option menu clicks
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_calender -> {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.MYTASKCALENDER.name)
            startActivity(intent)
            true
        }
        R.id.customtoolbar_notification -> {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )
            intent.putExtra(TYPE, EnumFromPage.NOTIFICATION.name)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun menueHide(boolean: Boolean) {
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finish()
        } else {

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)

            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isProfileChanged) {
            if (user_profileImg.isNotEmpty()) {
                Glide.with(this)
                    .load(user_profileImg)
                    .placeholder(R.drawable.ic_static_user)
                    .into(drawer_layout.drawerAgent.ivProfile)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_static_user)
                    .into(drawer_layout.drawerAgent.ivProfile)
            }
            drawer_layout.drawerAgent.tvProfileName.text = user_name
            drawer_layout.drawerAgent.tvProfileEmail.text = user_email
        }
    }
}
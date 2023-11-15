package com.ncomfortsagent.ui.main.home.home.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityHomeBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.start_up.auth.activity.AuthActivity
import com.ncomfortsagent.ui.main.home.home.adapter.HomeAdapter
import com.ncomfortsagent.ui.main.home.home.viewmodel.HomeViewModel
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity
import com.ncomfortsagent.ui.main.sideMenu.bankDetails.activity.BankDetailsActivity
import com.ncomfortsagent.ui.main.sideMenu.myProfile.activity.MyProfileActivity
import com.ncomfortsagent.utils.*
import com.ncomfortsagent.utils.AppPreferences.clearSharedPreference
import com.ncomfortsagent.utils.AppPreferences.prefProfileImage
import com.ncomfortsagent.utils.AppPreferences.prefUserEmail
import com.ncomfortsagent.utils.AppPreferences.prefUserName
import com.ncomfortsagent.utils.AppPreferences.user_lang
import com.ncomfortsagent.utils.CommonUtils.Companion.changeLanguageAwareContext
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import java.util.*
import kotlin.system.exitProcess


class HomeActivity : BaseActivity<ActivityHomeBinding>(), FragmentTransInterface {

    private lateinit var viewPagerAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var doubleBackToExitPressedOnce = false

    override val layoutId: Int
        get() = R.layout.activity_home
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    @SuppressLint("ResourceAsColor")
    override fun initData() {

        /* setup toolbar */
        setSupportActionBar(binding.appbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.elevation = 0F

        refreshProfile()

        homeViewModel.homeCount()

        /* Setup Tabs */

        binding.appbar.content.homeViewPager.adapter = HomeAdapter( supportFragmentManager, lifecycle)
        TabLayoutMediator(
            binding.appbar.tabs,
            binding.appbar.content.homeViewPager, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.my_property)
                    1 -> tab.text = resources.getString(R.string.enquiries)
                }
            }).attach()

        binding.appbar.tabs.getTabAt(1)!!.setCustomView(R.layout.layout_badge)
        binding.appbar.tabs.getTabAt(1)
        binding.appbar.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_property_gold)
        binding.appbar.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_message_gold)






        //setUpTabs()
        setUpTabIcons()

        /* Setup Tab Badges */
        /*   val badgeDrawable: BadgeDrawable =  binding.appbar.tabs.getTabAt(1)!!.orCreateBadge
           badgeDrawable.isVisible = true
           badgeDrawable.backgroundColor = R.color.burnt_sienna
           badgeDrawable.number = 2*/



        /* setup navigation drawer */
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appbar.toolbar,
            R.string.open,
            R.string.close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav_menu_icon)
        toggle.isDrawerSlideAnimationEnabled = true
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setUpTabIcons() {

    }

    private fun setUpTabs() {


       /* binding.appbar.tabs.addTab(
            binding.appbar.tabs.newTab().setText(getString(R.string.my_property))
        )
        binding.appbar.tabs.addTab(
            binding.appbar.tabs.newTab().setText(getString(R.string.enquiries))
        )*/

        /* binding.appbar.tabs.setBadgeText(0, null)

         binding.appbar.tabs.setBadgeText(2, "2")*/


        //binding.vpServiceMain.currentItem=0


      /*  viewPager.adapter = viewPagerAdapter

        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.appbar.tabs))

        binding.appbar.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })*/
    }


    override fun fragmentLaunch() {

    }

    override fun setupUI() {


    }


    override fun setupViewModel() {
        homeViewModel = HomeViewModel(this)
    }

    override fun setupObserver() {

        /* home count */
        homeViewModel.getAgentHomeCountResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val temp = it.data!!.response.toString().substringBefore(".")
                    if (temp == "0") {
                        binding.appbar.tabs.getTabAt(1)!!.customView?.findViewById<LinearLayout>(R.id.mainBadge)?.visibility =
                            View.GONE
                    } else {
                        binding.appbar.tabs.getTabAt(1)!!.customView?.findViewById<LinearLayout>(R.id.mainBadge)?.visibility =
                            View.VISIBLE
                    }
                    binding.appbar.tabs.getTabAt(1)!!.customView?.findViewById<TextView>(R.id.badgetv)?.text =
                        temp
                }
                Status.LOADING -> {
                    binding.appbar.tabs.getTabAt(1)!!.customView?.findViewById<LinearLayout>(R.id.mainBadge)?.visibility =
                        View.GONE
                }
                Status.DATA_EMPTY -> {
                  /*  showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )*/
                }
                Status.ERROR -> {
                   /* showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )*/
                }
                Status.NO_INTERNET -> {
                   /* if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }*/
                }
            }
        })


        /* logout api */
        homeViewModel.getAgentLogoutResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* clear shared preference */
                    clearSharedPreference()

                    /* navigate to login page */
                    startActivity(Intent(this, AuthActivity::class.java))
                    ActivityCompat.finishAffinity(this)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )

                }
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
            }
        })

    }

    override fun onClicks() {

        /* Side menu clicks */

        /* Change Language */
        binding.drawer.navLangSelect.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val items = arrayOf(getString(R.string.english), getString(R.string.arabic1))
            val materialBuilder = MaterialAlertDialogBuilder(this, R.style.custom_material_theme)
            materialBuilder.setTitle(getString(R.string.select_lang))
            materialBuilder.setItems(items) { dialog, which ->
                if (which == 0) {
                    changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                    user_lang = Constants.ENGLISH_LAG
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)

                }
                if (which == 1) {
                    changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                    user_lang = Constants.ARABIC_LAG
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)

                }
            }
            materialBuilder.show()
        }

        /* Bank Details */
        binding.drawer.navBankDetails.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, BankDetailsActivity::class.java)
            startActivity(intent)
        }

        /* My Profile */
        binding.drawer.navMyProfile.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        /* Notifications */
        binding.drawer.navNotificationDetails.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.NOTIFICATION.name)
            startActivity(intent)
        }

        /* My Properties */
        binding.drawer.navMyProperties.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        /* change password */
        binding.drawer.navChangePassword.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.CHANGE_PASSWORD.name)
            startActivity(intent)
        }

        /* Legal Information */
        binding.drawer.navLegalInformation.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.LEGAL_INFORMATION.name)
            startActivity(intent)
        }

        /* Privacy Policy */
        binding.drawer.navPrivacy.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.PRIVACY_POLICY.name)
            startActivity(intent)
        }

        /* FAQ */
        binding.drawer.navFaq.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.FAQ.name)
            startActivity(intent)
        }

        /* FeedBack */
        binding.drawer.navFeedback.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.FEEDBACK.name)
            startActivity(intent)
        }

        /* Logout */
        binding.drawer.navLogout.setOnClickListener {
            if(CommonUtils.isOpenRecently()) return@setOnClickListener
            showExitDialog()
        }

    }

    /* Logout Dialog */
    private fun showExitDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.logout_layout)

        val yesBtn = dialog.findViewById(R.id.okBtnLogout) as Button
        val cancelBtn = dialog.findViewById(R.id.cancelBtnLogout) as Button

        dialog.show()

        yesBtn.setOnClickListener {
            homeViewModel.logout()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setTitleFromFragment(title: String) {
        binding.appbar.tvToolbarTitle1.text = title
    }

    override fun onResume() {
        super.onResume()
        refreshProfile()
    }

    /* refresh drawer profile details */
    private fun refreshProfile() {
        Glide.with(this).load(prefProfileImage).placeholder(R.drawable.ic_profile_user)
            .into(binding.drawer.navProfileImg)
        binding.drawer.navProfileName.text = prefUserName
        binding.drawer.navProfileEmail.text = prefUserEmail

        homeViewModel.homeCount()
    }

    /* option menu setup */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* back press */
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } else {

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    doubleBackToExitPressedOnce = false
                }
            }, 2000)

            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

        }
    }

}
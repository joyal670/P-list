package com.property.propertyuser.ui.main.home.dashboard

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityDashboardBinding
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.preference.AppPreferences.user_profile_image
import com.property.propertyuser.ui.main.home.HomeViewModel
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.notification.NotificationActivity
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.about_us.AboutUsActivity
import com.property.propertyuser.ui.main.side_menu.faq.FAQActivity
import com.property.propertyuser.ui.main.side_menu.feedback.FeedbackActivity
import com.property.propertyuser.ui.main.side_menu.legal_information.LegalInformationActivity
import com.property.propertyuser.ui.main.side_menu.privacy_policy.PrivacyPolicyActivity
import com.property.propertyuser.ui.main.side_menu.user_profile.ProfileActivity
import com.property.propertyuser.ui.startup.auth.AuthActivity
import com.property.propertyuser.utils.*
import java.util.*
import kotlin.concurrent.schedule


class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private var arrowUP: Boolean = false
    private lateinit var homeViewModel: HomeViewModel
    private var dialog: Dialog? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var dashboardAdapter: DashboardAdapter
    private var dashboardNameList = ArrayList<String>()
    private var dashboardIconList = ArrayList<Int>()

    override val layoutId: Int
        get() = R.layout.activity_dashboard
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        if (!(AppPreferences.user_name.isNullOrBlank())) {
            binding.layoutSideMenu.tvUserName.text = AppPreferences.user_name!!
        }
        if (!(AppPreferences.user_email.isNullOrBlank())) {
            binding.layoutSideMenu.tvUserEmail.text = AppPreferences.user_email!!
        }

        Glide.with(this).load(user_profile_image).into(binding.layoutSideMenu.sivUserImage)

        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.include.rvDashboard.layoutManager = gridLayoutManager


        dashboardNameList.add(getString(R.string.tvMyProfile))
        dashboardNameList.add(getString(R.string.tvMyProperties))
        dashboardNameList.add(getString(R.string.tvFindProperty))
        dashboardNameList.add(getString(R.string.tvBecomeOwner))
        dashboardNameList.add(getString(R.string.about_us))
        dashboardNameList.add(getString(R.string.feedback))

        dashboardIconList.add(R.drawable.ic_my_profile)
        dashboardIconList.add(R.drawable.ic_my_property)
        dashboardIconList.add(R.drawable.ic_find_your_property)
        dashboardIconList.add(R.drawable.ic_become_an_owner)
        dashboardIconList.add(R.drawable.ic_about_us)
        dashboardIconList.add(R.drawable.ic_faq)
        dashboardAdapter =
            DashboardAdapter(dashboardNameList, dashboardIconList) { selectedItem(it) }
        binding.include.rvDashboard.adapter = dashboardAdapter
        binding.include.rvDashboard.scheduleLayoutAnimation()
    }

    private fun selectedItem(id: Int) {
        when (id) {
            0 -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }

            1 -> {
                startActivity(Intent(this, MyPropertyActivity::class.java))
            }

            2 -> {
                val items = ArrayList<String>()
                items.add(getString(R.string.find_property))
                items.add(getString(R.string.tvRequestedProperty))

                /* MaterialAlertDialogBuilder(this, R.style.ToolBarTheme)
                     .setTitle(resources.getString(R.string.btnSelect))
                     .setItems(items.toTypedArray()) { dialog, which ->
                         when (which) {
                             0 -> {
                                 val intent = Intent(this, SideMenuActivity::class.java)
                                 intent.putExtra(Constants.TYPE, TYPE_MENUE.FINDPROPERTY.name)
                                 startActivity(intent)
                             }
                             1 -> {
                                 val intent = Intent(this, SideMenuActivity::class.java)
                                 intent.putExtra(Constants.TYPE, TYPE_MENUE.REQUESTEDPROPERTY.name)
                                 startActivity(intent)
                             }
                         }
                     }
                     .show()*/

                setUpDialog()
            }

            3 -> {
                val intent = Intent(this, SideMenuActivity::class.java)
                intent.putExtra(Constants.TYPE, TYPE_MENUE.BECOMEOWNER.name)
                startActivity(intent)
            }

            4 -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
            }

            5 -> {
                startActivity(Intent(this, FeedbackActivity::class.java))
            }
        }
    }

    private fun setUpDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_select_option)

        val tvFindProperty = dialog.findViewById(R.id.tvFindProperty) as TextView
        val tvRequestedProperty = dialog.findViewById(R.id.tvRequestedProperty) as TextView
        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        tvFindProperty.setOnClickListener {
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.FINDPROPERTY.name)
            startActivity(intent)
            dialog.dismiss()
        }
        tvRequestedProperty.setOnClickListener {
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.REQUESTEDPROPERTY.name)
            startActivity(intent)
            dialog.dismiss()
        }
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel()
    }

    override fun setupObserver() {

        homeViewModel.getUserLogoutResponse().observe(this) {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    AppPreferences.logoutClearPreference()
                    dialog?.dismiss()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finishAffinity()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        }
    }

    override fun onClicks() {

        binding.include.ivMenuHome.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.layoutSideMenu.tvArabic.setOnClickListener {
            if (binding.layoutSideMenu.tvArabic.text == "English") {
                AppPreferences.chooseLanguage = "english"
                CommonMethods.changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                startActivity(Intent(this, DashboardActivity::class.java))
                overridePendingTransition(0, 0)
            } else {
                AppPreferences.chooseLanguage = "arabic"
                CommonMethods.changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                startActivity(Intent(this, DashboardActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        binding.layoutSideMenu.ivclose.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.layoutSideMenu.tvMyProfile.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.layoutSideMenu.tvFAQ.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FAQActivity::class.java))
        }

        binding.layoutSideMenu.tvAboutUs.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, AboutUsActivity::class.java))
        }
        binding.layoutSideMenu.tvPrivacyPolicy.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        binding.layoutSideMenu.tvLegalInformation.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, LegalInformationActivity::class.java))
        }

        binding.layoutSideMenu.tvFeedback.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        binding.layoutSideMenu.tvMyProperties.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, MyPropertyActivity::class.java))
        }

        binding.layoutSideMenu.tvBecomeOwner.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.BECOMEOWNER.name)
            startActivity(intent)
        }

        binding.layoutSideMenu.tvReferFriend.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.REFERAL.name)
            startActivity(intent)
        }
        binding.layoutSideMenu.tvRewards.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.REWARDS.name)
            startActivity(intent)
        }

        binding.layoutSideMenu.tvRequestedProperty.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.REQUESTEDPROPERTY.name)
            startActivity(intent)
        }
        binding.include.ivNotification.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        binding.layoutSideMenu.tvReferal.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (binding.layoutSideMenu.tvReferal.text == "Referral") {
                if (arrowUP) {
                    binding.layoutSideMenu.tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_group_gray,
                        0,
                        R.drawable.ic__arrow_down_24,
                        0
                    )
                    arrowUP = false
                    binding.layoutSideMenu.expandableLayout1.collapse()
                } else {
                    binding.layoutSideMenu.tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_group_gray,
                        0,
                        R.drawable.ic_arrow_up_24,
                        0
                    )
                    arrowUP = true
                    binding.layoutSideMenu.expandableLayout1.expand()
                }
            } else {
                if (arrowUP) {
                    binding.layoutSideMenu.tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic__arrow_down_24,
                        0,
                        R.drawable.ic_group_gray,
                        0
                    )
                    arrowUP = false
                    binding.layoutSideMenu.expandableLayout1.collapse()
                } else {
                    binding.layoutSideMenu.tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up_24,
                        0,
                        R.drawable.ic_group_gray,
                        0
                    )
                    arrowUP = true
                    binding.layoutSideMenu.expandableLayout1.expand()
                }
            }
        }

        binding.layoutSideMenu.tvLogout.setOnClickListener {
            openLogoutDialog()
        }

        binding.layoutSideMenu.tvNotification.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        var click = false
        binding.layoutSideMenu.tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_search_gray,
            0,
            R.drawable.ic__arrow_down_24,
            0
        )

        binding.layoutSideMenu.tvFindProperty.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (!click) {
                binding.layoutSideMenu.expandableLayout.expand()
                click = true
                binding.layoutSideMenu.tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search_gray,
                    0,
                    R.drawable.ic_arrow_up_24,
                    0
                )
            } else {
                binding.layoutSideMenu.expandableLayout.collapse()
                click = false
                binding.layoutSideMenu.tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search_gray,
                    0,
                    R.drawable.ic__arrow_down_24,
                    0
                )
            }
        }

        binding.layoutSideMenu.tvFind.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, TYPE_MENUE.FINDPROPERTY.name)
            startActivity(intent)
        }
    }

    private fun openLogoutDialog() {
        dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.layout_logout)

        val btnOk = dialog?.findViewById(R.id.btnOk) as Button
        val btnCancel = dialog?.findViewById(R.id.btnCancel) as Button
        val ivClose = dialog?.findViewById(R.id.ivClose) as ImageView

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams

        btnOk.setOnClickListener {
            homeViewModel.userLogout()
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        ivClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
            } else {

                this.doubleBackToExitPressedOnce = true
                binding.drawerLayout.snack(getString(R.string.toastPressBack))
                Timer().schedule(2000) {
                    doubleBackToExitPressedOnce = false
                }
            }
        }
    }

    override fun getViewBinging(): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(layoutInflater)

}
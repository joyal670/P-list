package com.iroid.jeetmeet.ui.main.parent_panel.home.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityParentDashboardBinding
import com.iroid.jeetmeet.listeners.ActivityListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.start_up.auth.activity.AuthActivity
import com.iroid.jeetmeet.start_up.auth.viewmodel.ParentLoginViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.home.fragment.ParentDashboardFragment
import com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel.ParentSideMenuViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.utils.*
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentImg
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentName
import kotlinx.android.synthetic.main.custom_drawer_parent_layout.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*
import kotlin.system.exitProcess

class ParentDashboardActivity : BaseActivity<ActivityParentDashboardBinding>() , ActivityListener,
    FragmentTransInterface {

    private var doubleBackToExitPressedOnce = false
    private lateinit var parentLoginViewModel: ParentLoginViewModel
    private lateinit var parentSideMenuViewModel: ParentSideMenuViewModel

    override val layoutId: Int
        get() = R.layout.activity_parent_dashboard
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityParentDashboardBinding {
        return ActivityParentDashboardBinding.inflate(layoutInflater)
    }

    override fun initData() {
        /* setup toolbar  */
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        /* nav drawer */
        val toggle = ActionBarDrawerToggle(this , binding.drawerLayout , toolbar , R.string.open , R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.isDrawerSlideAnimationEnabled = true
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if(binding.drawerLayout.isDrawerVisible(GravityCompat.START))
            {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else
            {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        fragmentLaunch()

        setupObserver()
    }

    override fun fragmentLaunch() {
        replaceFragment(fragment = ParentDashboardFragment())
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* side menu api */
        parentSideMenuViewModel = ParentSideMenuViewModel()
        parentSideMenuViewModel.parentSideMenu()
        parentSideMenuViewModel.parentSideMenuData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    Glide.with(this).load(it.data!!.parent.image_url).into(binding.dashboardNavView.ivProfilePic)
                    binding.dashboardNavView.tvParentName.text = it.data.parent.first_name + " " + it.data.parent.last_name

                }
                Status.LOADING ->{
                    showProgress()
                }
                Status.NO_INTERNET ->{
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY ->{
                    dismissProgress()
                }
                Status.ERROR ->{
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun onClicks() {

        /* side drawer buttons */
        /* personal button */
        var personalclick  = true
        val slideDownAnim : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
        val slideUpAnim : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        binding.dashboardNavView.personal_lv.setOnClickListener {
            if (personalclick)
            {
                personalclick = false
                binding.dashboardNavView.layout_personal.apply {
                    visibility = View.VISIBLE
                    // startAnimation(slideDownAnim)visible
                }
                binding.dashboardNavView.tvPersonal.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_top_grey,0)
            }
            else
            {
                personalclick = true
                binding.dashboardNavView.layout_personal.apply {
                    //   startAnimation(slideUpAnim)
                    visibility = View.GONE

                }
                binding.dashboardNavView.tvPersonal.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_down_grey,0)
            }

        }

        /* academic button */
        var acadamicclick = true
        binding.dashboardNavView.lvAcadamic.setOnClickListener {
            if(acadamicclick)
            {
                acadamicclick = false
                binding.dashboardNavView.layout_academic.apply {
                    visibility = View.VISIBLE
                    //startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvAcadamic.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_top_grey,0)
            }
            else
            {
                acadamicclick = true
                binding.dashboardNavView.layout_academic.apply {
                    visibility = View.GONE
                    //startAnimation(slideUpAnim)
                }

                binding.dashboardNavView.tvAcadamic.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down_grey,0)
            }


        }

        /* dashboard */
        binding.dashboardNavView.lvDashboard.setOnClickListener {
            replaceFragment(fragment = ParentDashboardFragment())
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        /* Profile page */
        binding.dashboardNavView.tvParentProfile.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.PROFILE.name)
            startActivity(intent)
        }

        /* Diaries page */
        binding.dashboardNavView.tvParentFeedBack.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.FEEDBACK.name)
            startActivity(intent)
        }

        /* Students page */
        binding.dashboardNavView.tvParentStudents.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENTS.name)
            startActivity(intent)
        }


        /* Assign Leave page */
        binding.dashboardNavView.tvLeaveApplication.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.LEAVE.name)
            startActivity(intent)
        }

        /* Assign Leave page */
        binding.dashboardNavView.tvParentTimeTabel.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.TIMETABLE.name)
            startActivity(intent)
        }

        /* Calender Leave page */
        binding.dashboardNavView.tvParentCalender.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.PARENT_CALENDER.name)
            startActivity(intent)
        }

        /* Attendance Leave page */
        binding.dashboardNavView.tvParentAttendace.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.ATTENDANCE.name)
            startActivity(intent)
        }

        /* Events Leave page */
        binding.dashboardNavView.tvParentEvents.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.EVENTS.name)
            startActivity(intent)
        }

        /* Notice page */
        binding.dashboardNavView.tvNotice.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.NOTICE.name)
            startActivity(intent)
        }

        /* Meetings */
        binding.dashboardNavView.tvMeetings.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.MEETINGS.name)
            startActivity(intent)
        }

        /* My Account */
        binding.dashboardNavView.tvMyAccount.setOnClickListener {
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.MY_ACCOUNT.name)
            startActivity(intent)
        }

        /* Logout */
        binding.dashboardNavView.tvLogout.setOnClickListener {
            showExitDialog()
        }
    }

    /* logout dialog */
    private fun showExitDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.logout_layout)

        val yesBtn = dialog.findViewById(R.id.okBtnLogout) as Button
        val cancelBtn = dialog.findViewById(R.id.cancelBtnLogout) as Button

        dialog.show()

        yesBtn.setOnClickListener {
            setupLogOutObserver()
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

    /* logout api */
    private fun setupLogOutObserver() {
        parentLoginViewModel = ParentLoginViewModel()
        parentLoginViewModel.parentLogoutApi()
        parentLoginViewModel.parentLogoutData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    /* clear saved data */
                    AppPreferences.clearSharedPreference()

                    /* navigate to login page */
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.LOADING ->{
                    showProgress()
                }
                Status.ERROR ->{
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY ->{
                    dismissProgress()
                }
                Status.NO_INTERNET ->{
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    /* set title */
    override fun setTitleFromFragment(title: String) {
        tvToolbarTitle.text = title
    }

    /* set text in center */
    override fun setTitleinCenter(center: Boolean) {
        if(center)
        {
            tvToolbarTitle.gravity = Gravity.CENTER
        }
        else
        {
            tvToolbarTitle.gravity = Gravity.START
        }
    }

    /* set capital */
    override fun setTitleCaptial(cap: Boolean) {
        if(cap)
        {
            tvToolbarTitle.isAllCaps = cap
        }
        else
        {
            tvToolbarTitle.isAllCaps = false
        }
    }

    /* set font family */
    override fun setFontFamily(fontFamily: Int) {
        tvToolbarTitle.typeface = ResourcesCompat.getFont(this, fontFamily)
    }

    /* option menu setup */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)
    {
        R.id.customtoolbar_search ->
        {
            true
        }
        R.id.customtoolbar_chat ->
        {
            /* TEACHER CHAT page */
            val intent= Intent(this, ParentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.TEACHER_CHAT.name)
            startActivity(intent)
            true

        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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

    override fun onResume() {
        super.onResume()
        if (prefStudentImg == "") {
            Glide.with(this).load(R.drawable.ic_profile_user)
                .into(binding.dashboardNavView.ivProfilePic)
        } else {
            Glide.with(this).load(prefStudentImg).into(binding.dashboardNavView.ivProfilePic)
        }

        if (prefStudentName == "") {
            binding.dashboardNavView.tvParentName.text = "Nill"
        } else {
            binding.dashboardNavView.tvParentName.text = prefStudentName
        }
    }
}
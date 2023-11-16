package com.iroid.jeetmeet.ui.main.student_panel.home.activity

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
import com.iroid.jeetmeet.databinding.ActivityStudentdashboardBinding
import com.iroid.jeetmeet.listeners.ActivityListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.listeners.ProfileUpdate
import com.iroid.jeetmeet.start_up.auth.activity.AuthActivity
import com.iroid.jeetmeet.start_up.auth.viewmodel.StudentLoginViewModel
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.home.fragment.StudentDashboardFragment
import com.iroid.jeetmeet.ui.main.student_panel.home.viewmodel.StudentProfileViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.utils.*
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentImg
import kotlinx.android.synthetic.main.custom_drawer_layout.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*
import kotlin.system.exitProcess

class StudentDashboardActivity : BaseActivity<ActivityStudentdashboardBinding>(), ActivityListener,
    FragmentTransInterface, ProfileUpdate {
    private lateinit var userPreferences: DataStoreUserPreferences
    private var doubleBackToExitPressedOnce = false
    private lateinit var studentProfileViewModel: StudentProfileViewModel
    private lateinit var studentLogoutViewModel: StudentLoginViewModel

    override val layoutId: Int
        get() = R.layout.activity_studentdashboard
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityStudentdashboardBinding =
        ActivityStudentdashboardBinding.inflate(layoutInflater)

    override fun initData() {
        userPreferences = DataStoreUserPreferences(this)
    }

    override fun fragmentLaunch() {
        replaceFragment(fragment = StudentDashboardFragment())
    }

    override fun setupUI() {
        /* setup toolbar  */
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        /* nav drawer */
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.isDrawerSlideAnimationEnabled = true
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        /* load profile img and name in side drawer */
        setupObserver()

        fragmentLaunch()
    }

    private fun loadProfileDetails() {
        if (prefStudentImg == "") {
            Glide.with(this).load(R.drawable.ic_profile_user)
                .into(binding.dashboardNavView.ivDrawerProfile)
        } else {
            Glide.with(this).load(prefStudentImg).into(binding.dashboardNavView.ivDrawerProfile)
        }

        if (AppPreferences.prefStudentFirstName == "") {
            binding.dashboardNavView.tvDashboardName.text = "Nill"
        } else {
            binding.dashboardNavView.tvDashboardName.text = AppPreferences.prefStudentFirstName
        }
    }

    override fun setupViewModel() {
    }

    /* load profile api */
    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        studentProfileViewModel = StudentProfileViewModel(this)
        studentProfileViewModel.studentProfileApi()
        studentProfileViewModel.studentProfileData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* load data */
                    Glide.with(this).load(it.data!!.data.profile_image_url).into(binding.dashboardNavView.ivDrawerProfile)
                    binding.dashboardNavView.tvDashboardName.text =  it.data.data.first_name

                }
                Status.LOADING -> {
                    showProgress()

                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
            }
        })
    }

    /* logout api */
    private fun setupLogOutObserver() {
        studentLogoutViewModel = StudentLoginViewModel()
        studentLogoutViewModel.studentLogoutApi()
        studentLogoutViewModel.studentLogoutData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* clear saved data */
                    AppPreferences.clearSharedPreference()

                    /* navigate to login page */
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
            }
        })
    }

    override fun onClicks() {

        /* side drawer buttons */
        /* personal button */
        var personalclick = true
        val slideDownAnim: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
        val slideUpAnim: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        binding.dashboardNavView.personal_lv.setOnClickListener {
            if (personalclick) {
                personalclick = false
                binding.dashboardNavView.layout_personal.apply {
                    visibility = View.VISIBLE
                    // startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvPersonal.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                personalclick = true
                binding.dashboardNavView.layout_personal.apply {
                    //   startAnimation(slideUpAnim)
                    visibility = View.GONE

                }
                binding.dashboardNavView.tvPersonal.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }

        }

        /* application leave button */
        var leaveclick = true
        binding.dashboardNavView.tvLeaveApplication.setOnClickListener {
            if (leaveclick) {
                leaveclick = false
                binding.dashboardNavView.layout_leave.apply {
                    visibility = View.VISIBLE
                    //startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvLeaveApplication.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                leaveclick = true
                binding.dashboardNavView.layout_leave.apply {
                    visibility = View.GONE
                    //startAnimation(slideUpAnim)
                }
                binding.dashboardNavView.tvLeaveApplication.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }
        }

        /* academic button */
        var acadamicclick = true
        binding.dashboardNavView.lvAcadamic.setOnClickListener {
            if (acadamicclick) {
                acadamicclick = false
                binding.dashboardNavView.layout_academic.apply {
                    visibility = View.VISIBLE
                    //startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvAcadamic.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                acadamicclick = true
                binding.dashboardNavView.layout_academic.apply {
                    visibility = View.GONE
                    //startAnimation(slideUpAnim)
                }

                binding.dashboardNavView.tvAcadamic.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }
        }

        /* library button */
        var libraryclick = true
        binding.dashboardNavView.lvlibrary.setOnClickListener {
            if (libraryclick) {
                libraryclick = false
                binding.dashboardNavView.layout_library.apply {
                    visibility = View.VISIBLE
                    //startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvlibrary.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                libraryclick = true
                binding.dashboardNavView.layout_library.apply {
                    visibility = View.GONE
                    //startAnimation(slideUpAnim)
                }
                binding.dashboardNavView.tvlibrary.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }
        }

        /* Mock Test button */
        var mockClick = true
        binding.dashboardNavView.lvMockTest.setOnClickListener {
            if (mockClick) {
                mockClick = false
                binding.dashboardNavView.layout_mock_test.apply {
                    visibility = View.VISIBLE
                }
                binding.dashboardNavView.tvMockTest.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                mockClick = true
                binding.dashboardNavView.layout_mock_test.apply {
                    visibility = View.GONE
                }
                binding.dashboardNavView.tvMockTest.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }
        }

        /* exam button */
        var examclick = true
        binding.dashboardNavView.lvexam.setOnClickListener {
            if (examclick) {
                examclick = false
                binding.dashboardNavView.layout_exam.apply {
                    visibility = View.VISIBLE
                    //startAnimation(slideDownAnim)
                }
                binding.dashboardNavView.tvExam.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
            } else {
                examclick = true
                binding.dashboardNavView.layout_exam.apply {
                    visibility = View.GONE
                    //startAnimation(slideUpAnim)
                }
                binding.dashboardNavView.tvExam.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
            }
        }

        /* dashboard */
        binding.dashboardNavView.dashboard_lv.setOnClickListener {
            replaceFragment(fragment = StudentDashboardFragment())
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        /* Profile page */
        binding.dashboardNavView.tvStudentProfile.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.PROFILE.name)
            startActivity(intent)
        }

        /* Diaries page */
        binding.dashboardNavView.tvStudentDiaries.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.DIARY.name)
            startActivity(intent)
        }

        /* Staff directory page */
        binding.dashboardNavView.tvStaffDirectory.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STAFF_DIRECTORY.name)
            startActivity(intent)
        }

        /* Calender */
        binding.dashboardNavView.tvStudentCalender.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_CALENDER.name)
            startActivity(intent)
        }

        /* Assigned leave */
        binding.dashboardNavView.tvStudentAssignedLeave.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.ASSIGNED_LEAVE.name)
            startActivity(intent)
        }

        /* Leave apply */
        binding.dashboardNavView.tvStudentLeaveApply.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.LEAVE_APPLY.name)
            startActivity(intent)
        }

        /* Request book */
        binding.dashboardNavView.tvStudentRequestBook.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.REQUEST_BOOK.name)
            startActivity(intent)
        }

        /* Issued book */
        binding.dashboardNavView.tvStudentIssuedBook.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.ISSUED_BOOK.name)
            startActivity(intent)
        }

        /* Test Scheduled */
        binding.dashboardNavView.tvStudentTestScheduled.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.TEST_SCHEDULED.name)
            startActivity(intent)
        }

        /* Test Result */
        binding.dashboardNavView.tvStudentTestResult.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.TEST_RESULT.name)
            startActivity(intent)
        }

        /* student assignment */
        binding.dashboardNavView.tvStudentAssignment.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_ASSIGNMENT.name)
            startActivity(intent)
        }

        /* student subject */
        binding.dashboardNavView.tvStudentSubject.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_SUBJECT.name)
            startActivity(intent)
        }

        /* student library */
        binding.dashboardNavView.tvStudentLibrary.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_LIBRARY.name)
            startActivity(intent)
        }

        /* student time table */
        binding.dashboardNavView.tvStudentTimeTable.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_TIMETABLE.name)
            startActivity(intent)
        }

        /* student time table */
        binding.dashboardNavView.tvStudentAttendance.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_ATTENDANCE.name)
            startActivity(intent)
        }

        /* student online class */
        binding.dashboardNavView.tvStudentOnlineClass.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_ONLINE_CLASS.name)
            startActivity(intent)
        }

        /* student attend exam */
        binding.dashboardNavView.tvStudentAttendExam.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_ATTEND_EXAM.name)
            startActivity(intent)
        }

        /* Student exam result */
        binding.dashboardNavView.tvStudentExamResult.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_EXAM_RESULT.name)
            startActivity(intent)
        }

        /* student event */
        binding.dashboardNavView.tvStudentEvents.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_EVENTS.name)
            startActivity(intent)
        }

        /* ExamSchedule exam */
        binding.dashboardNavView.rvStudentExamSchedule.setOnClickListener {
            val intent = Intent(this, StudentSideMenuActivity::class.java)
            intent.putExtra(Constants.TYPE, EnumFromPage.STUDENT_EXAM_SCHEDULE.name)
            startActivity(intent)
        }

        binding.dashboardNavView.tvStudentLogout.setOnClickListener {
            showExitDialog()
            /* lifecycleScope.launch {
                 userPreferences.set_is_login(false)
             }*/
            /*  val intent= Intent(this, AuthActivity::class.java)
              startActivity(intent)
              finish()*/
        }


    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    /* set title */
    override fun setTitleFromFragment(title: String) {
        tvToolbarTitle.text = title
    }

    /* set text in center */
    override fun setTitleinCenter(center: Boolean) {
        if (center) {
            tvToolbarTitle.gravity = Gravity.CENTER
        } else {
            tvToolbarTitle.gravity = Gravity.START
        }
    }

    /* set capital */
    override fun setTitleCaptial(cap: Boolean) {
        if (cap) {
            tvToolbarTitle.isAllCaps = cap
        } else {
            tvToolbarTitle.isAllCaps = false
        }
    }

    /* set font family */
    override fun setFontFamily(fontFamily: Int) {
        tvToolbarTitle.typeface = ResourcesCompat.getFont(this, fontFamily)
    }

    /* option menu setup */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {
            true
        }
        R.id.customtoolbar_chat -> {
            val intent = Intent(this, StudentChatActivity::class.java)
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


    override fun onResume() {
        super.onResume()
        loadProfileDetails()
    }

    override fun refreshProfile() {
        loadProfileDetails()
    }

}
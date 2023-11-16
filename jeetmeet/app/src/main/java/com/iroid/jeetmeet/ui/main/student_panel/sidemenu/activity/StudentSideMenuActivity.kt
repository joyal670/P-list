package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity

import android.view.Gravity
import android.view.Menu
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityStudentSidemenuBinding
import com.iroid.jeetmeet.listeners.ActivityListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.assignment.StudentAssignmentFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.attendance.StudentAttendanceFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.events.StudentEventsFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.subject.StudentSubjectFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.time_table.StudentTimeTableFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.attend_exam.StudentAttendExamFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_result.StudentExamResultFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule.StudentExamScheduleFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.library.issued_book.StudentIssuedBookFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.library.request_book.StudentRequestBookFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_result.StudentTestResultFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled.StudentTestScheduleFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.online_class.StudentOnlineClassFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.calender.StudentCalenderFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.main_page.StudentLeaveApplicationFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.my_diaries.StudentDiaryFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.my_profile.StudentProfileFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.staff_directory.StudentStaffDirectoryFragment
import com.iroid.jeetmeet.utils.Constants.TYPE
import com.iroid.jeetmeet.utils.EnumFromPage
import com.iroid.jeetmeet.utils.replaceFragment
import kotlinx.android.synthetic.main.toolbar_main.*


class StudentSideMenuActivity : BaseActivity<ActivityStudentSidemenuBinding>(), ActivityListener,
    FragmentTransInterface {
    override val layoutId: Int
        get() = R.layout.activity_student_sidemenu
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityStudentSidemenuBinding =
        ActivityStudentSidemenuBinding.inflate(layoutInflater)

    override fun initData() {
        /* toolbar setup */
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        lanuchFragment(intent.getStringExtra(TYPE))
    }

    /* launch fragments */
    private fun lanuchFragment(stringExtra: String?) {
        when (stringExtra) {

            /* profile fragment */
            EnumFromPage.PROFILE.name -> {
                replaceFragment(fragment = StudentProfileFragment())
            }

            /* diary fragment */
            EnumFromPage.DIARY.name -> {
                replaceFragment(fragment = StudentDiaryFragment())
            }

            /* Staff directory fragment */
            EnumFromPage.STAFF_DIRECTORY.name -> {
                replaceFragment(fragment = StudentStaffDirectoryFragment())
            }

            /* Student calender */
            EnumFromPage.STUDENT_CALENDER.name -> {
                replaceFragment(fragment = StudentCalenderFragment())
            }

            /* Assigned leave */
            EnumFromPage.ASSIGNED_LEAVE.name -> {
                replaceFragment(fragment = StudentLeaveApplicationFragment.newInstance(0))
            }

            /* Leave apply */
            EnumFromPage.LEAVE_APPLY.name -> {
                replaceFragment(fragment = StudentLeaveApplicationFragment.newInstance(1))
            }

            /* Request book */
            EnumFromPage.REQUEST_BOOK.name -> {
                replaceFragment(fragment = StudentRequestBookFragment())
            }

            /* Issued book */
            EnumFromPage.ISSUED_BOOK.name -> {
                replaceFragment(fragment = StudentIssuedBookFragment())
            }

            /* student assignment */
            EnumFromPage.STUDENT_ASSIGNMENT.name -> {
                replaceFragment(fragment = StudentAssignmentFragment())
            }

            /* student subject */
            EnumFromPage.STUDENT_SUBJECT.name -> {
                replaceFragment(fragment = StudentSubjectFragment())
            }

            /* student time table */
            EnumFromPage.STUDENT_TIMETABLE.name -> {
                replaceFragment(fragment = StudentTimeTableFragment())
            }

            /* student attendance */
            EnumFromPage.STUDENT_ATTENDANCE.name -> {
                replaceFragment(fragment = StudentAttendanceFragment())
            }

            /* student online class */
            EnumFromPage.STUDENT_ONLINE_CLASS.name -> {
                replaceFragment(fragment = StudentOnlineClassFragment())
            }

            /* student attend exam  */
            EnumFromPage.STUDENT_ATTEND_EXAM.name -> {
                replaceFragment(fragment = StudentAttendExamFragment())
            }

            /* student exam result */
            EnumFromPage.STUDENT_EXAM_RESULT.name -> {
                replaceFragment(fragment = StudentExamResultFragment())
            }

            /* Student events */
            EnumFromPage.STUDENT_EVENTS.name -> {
                replaceFragment(fragment = StudentEventsFragment())
            }

            /* STUDENT_EXAM_SCHEDULE  class */
            EnumFromPage.STUDENT_EXAM_SCHEDULE.name -> {
                replaceFragment(fragment = StudentExamScheduleFragment())
            }

            /* Test Schedule */
            EnumFromPage.TEST_SCHEDULED.name -> {
                replaceFragment(fragment = StudentTestScheduleFragment())
            }

            /* Test Result */
            EnumFromPage.TEST_RESULT.name -> {
                replaceFragment(fragment = StudentTestResultFragment())
            }

        }
    }


    override fun fragmentLaunch() {
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

    /* *//* option menu  *//*
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {
            true
        }
        R.id.customtoolbar_chat -> {
            val intent= Intent(this, StudentChatActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }*/

}
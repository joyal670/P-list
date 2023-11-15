package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityParentSideMenuBinding
import com.iroid.jeetmeet.listeners.ActivityListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.parent_panel.chat.ParentTeacherChatFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount.MyAccountFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.attendance.ParentAttendanceFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.events.ParentEventsFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table.ParentTimeTableFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.meetings.ParentMeetingsFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.notice.ParentNoticeFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.assigned_leave.ParentAssignLeaveFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.calender.ParentCalenderFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_feedback.ParentFeedBackFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_profile.ParentProfileFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students.ParentStudentsFragment
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.EnumFromPage
import com.iroid.jeetmeet.utils.replaceFragment
import kotlinx.android.synthetic.main.toolbar_main.*

class ParentSideMenuActivity : BaseActivity<ActivityParentSideMenuBinding>(), ActivityListener,
    FragmentTransInterface {

    override val layoutId: Int
        get() = R.layout.activity_parent_side_menu

    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityParentSideMenuBinding =
        ActivityParentSideMenuBinding.inflate(layoutInflater)

    override fun initData() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        lanuchFragment(intent.getStringExtra(Constants.TYPE))
    }

    /* launch fragments */
    private fun lanuchFragment(stringExtra: String?) {
        when (stringExtra) {
            /* profile fragment */
            EnumFromPage.PROFILE.name -> {
                replaceFragment(fragment = ParentProfileFragment())
            }

            /* FEEDBACK fragment */
            EnumFromPage.FEEDBACK.name -> {
                replaceFragment(fragment = ParentFeedBackFragment())
            }

            /* STUDENTS  fragment */
            EnumFromPage.STUDENTS.name -> {
                replaceFragment(fragment = ParentStudentsFragment())
            }

            /* LEAVE  fragment */
            EnumFromPage.LEAVE.name -> {
                replaceFragment(fragment = ParentAssignLeaveFragment())
            }

            /* TIMETABLE  fragment */
            EnumFromPage.TIMETABLE.name -> {
                replaceFragment(fragment = ParentTimeTableFragment())
            }

            /* Calender  fragment */
            EnumFromPage.PARENT_CALENDER.name -> {
                replaceFragment(fragment = ParentCalenderFragment())
            }
            /* Attendance  fragment */
            EnumFromPage.ATTENDANCE.name -> {
                replaceFragment(fragment = ParentAttendanceFragment())
            }
            /* EVENTS  fragment */
            EnumFromPage.EVENTS.name -> {
                replaceFragment(fragment = ParentEventsFragment())
            }
            /* Teacher Chat  fragment */
            EnumFromPage.TEACHER_CHAT.name -> {
                replaceFragment(fragment = ParentTeacherChatFragment())
            }
            /* Meeting fragment */
            EnumFromPage.MEETINGS.name -> {
                replaceFragment(fragment = ParentMeetingsFragment())
            }
            /* Notice fragment */
            EnumFromPage.NOTICE.name -> {
                replaceFragment(fragment = ParentNoticeFragment())
            }
            /* My Account fragment */
            EnumFromPage.MY_ACCOUNT.name -> {
                replaceFragment(fragment = MyAccountFragment())
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

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {
            true
        }
        R.id.customtoolbar_chat -> {
            replaceFragment(fragment = ParentTeacherChatFragment(), addToBackStack = true)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}
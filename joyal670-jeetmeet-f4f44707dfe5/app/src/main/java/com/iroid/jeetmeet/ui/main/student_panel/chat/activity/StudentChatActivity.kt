package com.iroid.jeetmeet.ui.main.student_panel.chat.activity

import android.view.Gravity
import androidx.core.content.res.ResourcesCompat
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityStudentChatBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.chat.fragment.StudentAdminChatFragment
import com.iroid.jeetmeet.ui.main.student_panel.chat.fragment.StudentTeacherChatFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.toolbar_main.*

class StudentChatActivity : BaseActivity<ActivityStudentChatBinding>(),
    FragmentTransInterface {
    override val layoutId: Int
        get() = R.layout.activity_student_chat
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityStudentChatBinding =
        ActivityStudentChatBinding.inflate(layoutInflater)

    override fun initData() {
        /* toolbar setup */
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {
        /* tabs setup */
        binding.studentChattabs.setupWithViewPager(binding.studentChatViewpager)
        val adapter = ViewPagerAdapter(supportFragmentManager).also {
            it.addFragment(StudentAdminChatFragment(), getString(R.string.admin_chat))
            it.addFragment(StudentTeacherChatFragment(), getString(R.string.teacher_chat))
        }
        binding.studentChatViewpager.adapter = adapter
        binding.studentChattabs.setupWithViewPager(binding.studentChatViewpager)
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
    }


    override fun setTitleFromFragment(title: String) {
        tvToolbarTitle.text = title
    }

    override fun setTitleinCenter(center: Boolean) {
        if (center) {
            tvToolbarTitle.gravity = Gravity.CENTER
        } else {
            tvToolbarTitle.gravity = Gravity.START
        }
    }

    override fun setTitleCaptial(cap: Boolean) {
        if (cap) {
            tvToolbarTitle.isAllCaps = cap
        } else {
            tvToolbarTitle.isAllCaps = false
        }
    }

    override fun setFontFamily(fontFamily: Int) {
        tvToolbarTitle.typeface = ResourcesCompat.getFont(this, fontFamily)
    }

}
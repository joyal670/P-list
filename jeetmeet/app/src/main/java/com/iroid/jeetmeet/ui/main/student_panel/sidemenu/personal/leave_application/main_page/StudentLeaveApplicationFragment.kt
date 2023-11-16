package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentLeaveApplicationBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.adapter.ViewPagerAdapter
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.assigned_leave.StudentLeaveAssignedFragment
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.leave_apply.StudentLeaveApplyFragment


class StudentLeaveApplicationFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentLeaveApplicationBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface


    companion object {

        private const val TAG = "StudentFragment"
        private var type: Int? = null

        @JvmStatic
        fun newInstance(type: Int) =
            StudentLeaveApplicationFragment().apply {
                arguments = Bundle().apply {
                    putInt("user", type)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("user")
        }

    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentLeaveApplicationBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

    }

    override fun setupUI() {

        /* tabs setup */
        binding.studentleavetabs.setupWithViewPager(binding.studentLeaveViewpager)
        val adapter = ViewPagerAdapter(parentFragmentManager).also {
            it.addFragment(StudentLeaveAssignedFragment(), getString(R.string.assigned_leave))
            it.addFragment(StudentLeaveApplyFragment(), getString(R.string.leave_apply))
        }
        binding.studentLeaveViewpager.adapter = adapter
        binding.studentleavetabs.setupWithViewPager(binding.studentLeaveViewpager)
        launchFragment(type)

        /* change title in toolbar */
        binding.studentleavetabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        //tvToolbarTitle.text = getString(R.string.assigned_leave)

                        /* setup toolbar  */
                        fragmentTransInterface = activity as StudentSideMenuActivity
                        fragmentTransInterface.setTitleFromFragment(getString(R.string.assigned_leave))
                        fragmentTransInterface.setTitleinCenter(false)
                        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
                        fragmentTransInterface.setTitleCaptial(false)
                    }
                    1 -> {
                        //tvToolbarTitle.text = getString(R.string.leave)

                        /* setup toolbar  */
                        fragmentTransInterface = activity as StudentSideMenuActivity
                        fragmentTransInterface.setTitleFromFragment(getString(R.string.leave))
                        fragmentTransInterface.setTitleinCenter(false)
                        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
                        fragmentTransInterface.setTitleCaptial(false)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun launchFragment(type: Int?) {
        when (type) {
            /* launch assigned fragment */
            0 -> {
                binding.studentleavetabs.getTabAt(0)?.select()

                /* setup toolbar  */
                fragmentTransInterface = activity as StudentSideMenuActivity
                fragmentTransInterface.setTitleFromFragment(getString(R.string.assigned_leave))
                fragmentTransInterface.setTitleinCenter(false)
                fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
                fragmentTransInterface.setTitleCaptial(false)
            }

            /* launch leave fragment */
            1 -> {
                binding.studentleavetabs.getTabAt(1)?.select()

                /* setup toolbar  */
                fragmentTransInterface = activity as StudentSideMenuActivity
                fragmentTransInterface.setTitleFromFragment(getString(R.string.leave))
                fragmentTransInterface.setTitleinCenter(false)
                fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
                fragmentTransInterface.setTitleCaptial(false)
            }

        }

    }


    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

}
package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.staff_directory

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentStaffDirectoryBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.staff_directory.StudentStaffDirectoryData
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentStaffDirectoryViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentStaffDirectoryFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentStaffDirectoryBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentStaffDirectoryViewModel: StudentStaffDirectoryViewModel
    private var staffDirectoryData = ArrayList<StudentStaffDirectoryData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentStaffDirectoryBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.staff_directory))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentSubjectTeacher.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentStaffDirectoryViewModel = StudentStaffDirectoryViewModel()
        studentStaffDirectoryViewModel.studentStaffDirectory()
        studentStaffDirectoryViewModel.studentStaffDirectoryData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    staffDirectoryData.clear()
                    staffDirectoryData.addAll(it.data!!.data)

                    binding.rvStudentSubjectTeacher.adapter =
                        StudentStaffDirectoryAdapter(staffDirectoryData)

                    if (staffDirectoryData.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun onClicks() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }

}
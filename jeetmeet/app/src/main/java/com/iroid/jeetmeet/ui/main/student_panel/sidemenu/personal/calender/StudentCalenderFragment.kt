package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.calender

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentCalenderBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsAssignment
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentAssignmentsViewModel
import com.iroid.jeetmeet.utils.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class StudentCalenderFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentCalenderBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentAssignmentsViewModel: StudentAssignmentsViewModel
    private lateinit var studentAssignmentAdapter: StudentAssignmentAdapter
    private var assignmentsList = ArrayList<StudentAssignmentsAssignment>()
    var calenderType = arrayListOf<String>()
    val completedDates = ArrayList<CalendarDay>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentCalenderBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.calender))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        val instance = LocalDate.now()
        binding.studentcalendarView.setSelectedDate(instance)
        binding.rvstudentAssignment.layoutManager = LinearLayoutManager(context)

        setupObserver()


    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentAssignmentsViewModel = StudentAssignmentsViewModel()
        studentAssignmentsViewModel.studentAssignment(0)
        studentAssignmentsViewModel.studentAssignmentData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    assignmentsList.clear()
                    assignmentsList.addAll(it.data!!.data.assignments)
                    studentAssignmentAdapter = StudentAssignmentAdapter(assignmentsList)
                    binding.rvstudentAssignment.adapter = studentAssignmentAdapter

                    binding.studentcalendarView.addDecorators(
                        EventDecorator(activity, getEvents()),
                        MySelectorDecorator(activity),
                        OneDayDecorator()
                    )

                    if (assignmentsList.size == 0) {
                        binding.lvNoData.visibility = View.VISIBLE
                        binding.rvstudentAssignment.visibility = View.GONE
                        binding.studentcalendarView.visibility = View.GONE
                    } else {
                        binding.lvNoData.visibility = View.GONE
                        binding.rvstudentAssignment.visibility = View.VISIBLE
                        binding.studentcalendarView.visibility = View.VISIBLE
                    }

                }
                Status.LOADING -> {
                    showProgress()
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
                Status.NO_INTERNET -> {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            assignmentsList.clear()
            studentAssignmentAdapter.notifyDataSetChanged()
            setupObserver()
        }
    }

    private fun getEvents(): ArrayList<CalendarDay> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        assignmentsList.forEach {
            val localDate = LocalDate.parse(it.deadline_date.take(10), formatter)
            val day = CalendarDay.from(localDate)
            completedDates.add(day)
        }
        return completedDates
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
        item2.isVisible = true

    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {

            true
        }
        R.id.customtoolbar_chat -> {
            val intent = Intent(requireContext(), StudentChatActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.attendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentAttendanceBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.attendance.StudentAttendanceAttandance
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentAttendanceViewModel
import com.iroid.jeetmeet.utils.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class StudentAttendanceFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentAttendanceBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentAttendanceViewModel: StudentAttendanceViewModel
    private var attendanceList = ArrayList<StudentAttendanceAttandance>()
    private var attendancePresentList = ArrayList<StudentAttendanceAttandance>()
    private var attendanceLeaveList = ArrayList<StudentAttendanceAttandance>()
    val completedDates = ArrayList<CalendarDay>()
    val pauseDates = ArrayList<CalendarDay>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAttendanceBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.attadence))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        val instance = LocalDate.now()
        binding.studentAttendanceCalender.setSelectedDate(instance)

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentAttendanceViewModel = StudentAttendanceViewModel()
        studentAttendanceViewModel.studentAttendance()
        studentAttendanceViewModel.studentAttendanceData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    attendanceList.clear()
                    attendanceList.addAll(it.data!!.attandance)

                    attendancePresentList.clear()
                    attendanceLeaveList.clear()

                    attendanceList.forEach {
                        if (it.status == "P" || it.status == "L" || it.status == "LE") {
                            attendancePresentList.addAll(listOf(it))
                        } else if (it.status == "A") {
                            attendanceLeaveList.addAll(listOf(it))
                        }
                    }

                    Log.e("TAG", "setupObserver: attendanceList----$attendanceList")
                    Log.e("TAG", "setupObserver: attendancePresentList----$attendancePresentList")
                    Log.e("TAG", "setupObserver: attendanceLeaveList----$attendanceLeaveList")

                    binding.studentAttendanceCalender.addDecorators(
                        EventDecorator(
                            activity,
                            getAbsentEvents()
                        ),
                        MySelectorDecorator(activity),
                        OneDayDecorator(),
                        EventAttendanceDecorator(activity, getPresentEvents())
                    )

                    if (attendancePresentList.size == 0 && attendanceLeaveList.size == 0) {
                        binding.lvNoData.visibility = View.VISIBLE
                        binding.studentAttendanceCalender.visibility = View.GONE
                        binding.absentLayout.visibility = View.GONE
                        binding.presentLayout.visibility = View.GONE
                    } else {
                        binding.lvNoData.visibility = View.GONE
                        binding.studentAttendanceCalender.visibility = View.VISIBLE
                        binding.absentLayout.visibility = View.VISIBLE
                        binding.presentLayout.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    private fun getAbsentEvents(): ArrayList<CalendarDay> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        attendanceLeaveList.forEach {
            val localDate = LocalDate.parse(it.date, formatter)
            val day = CalendarDay.from(localDate)
            completedDates.add(day)
        }
        return completedDates
    }

    private fun getPresentEvents(): ArrayList<CalendarDay> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        attendancePresentList.forEach {
            val localDate = LocalDate.parse(it.date, formatter)
            val day = CalendarDay.from(localDate)
            pauseDates.add(day)
        }
        return pauseDates
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
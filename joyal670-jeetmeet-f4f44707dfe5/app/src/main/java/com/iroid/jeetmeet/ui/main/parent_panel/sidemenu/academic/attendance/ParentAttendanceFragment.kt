package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.attendance

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentAttendanceBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.attaendance.ParentAttendanceAttandance
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table.TimeTablePageAdaptor
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentAttendanceViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentStudentsListViewModel
import com.iroid.jeetmeet.utils.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class ParentAttendanceFragment : BaseFragment() {

    private lateinit var binding: FragmentParentAttendanceBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentStudentsListViewModel: ParentStudentsListViewModel
    private var studentList = ArrayList<ParentStudentsListData>()
    private lateinit var parentAttandanceViewModel: ParentAttendanceViewModel
    private var attendanceList = ArrayList<ParentAttendanceAttandance>()
    private var attendancePresentList = ArrayList<ParentAttendanceAttandance>()
    private var attendanceLeaveList = ArrayList<ParentAttendanceAttandance>()

    var studentNameList = ArrayList<String>()
    var studentIdList = ArrayList<Int>()

    private var selectedId = 0
    private var selectedName: String = ""

    var calenderType = arrayListOf<String>()
    val completedDates = ArrayList<CalendarDay>()
    val pauseDates = ArrayList<CalendarDay>()


    private lateinit var timeTablePageAdaptor: TimeTablePageAdaptor

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentAttendanceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParentAttendanceBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.attendance))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        val instance = LocalDate.now()
        binding.attendanceCalendarView.setSelectedDate(instance)

        setupObserver()

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* students list api */
        parentStudentsListViewModel = ParentStudentsListViewModel()
        parentStudentsListViewModel.parentStudentsList()
        parentStudentsListViewModel.parentStudentsListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    studentList.clear()
                    studentNameList.clear()
                    studentIdList.clear()

                    studentList.addAll(it.data!!.data)

                    it.data.data.forEach {
                        studentNameList.addAll(listOf(it.first_name))
                        studentIdList.addAll(listOf(it.id))
                    }

                    binding.studentSpinner.clearSelectedItem()
                    binding.studentSpinner.setItems(studentNameList)
                    if (studentIdList.size > 0) {
                        binding.studentSpinner.selectItemByIndex(0)
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

        /* spinner listener */
        binding.studentSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedId = 0
            selectedId = studentIdList[newIndex]
            selectedName = newText

            Log.e("TAG", "onClicks: selectedId$selectedId----selectedName$selectedName")

            studentList.forEach {
                if (selectedId == it.id) {
                    binding.tvStudentName.text = it.first_name
                    Glide.with(requireContext()).load(it.profile_image_url)
                        .into(binding.circleImageView)
                }
            }

            setUpCalenderObserver()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    /* calender api */
    private fun setUpCalenderObserver() {
        Log.e("TAG", "setUpCalenderObserver: $selectedId")

        parentAttandanceViewModel = ParentAttendanceViewModel()
        parentAttandanceViewModel.parentAttendance(selectedId)
        parentAttandanceViewModel.parentAttendanceData.observe(this, Observer {
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

                    if (attendanceList.size == 0) {
                        binding.lvNoData.visibility = View.VISIBLE
                        binding.absentLayout.visibility = View.GONE
                        binding.presentLayout.visibility = View.GONE
                        binding.attendanceCalendarViewLayout.visibility = View.GONE
                    } else {
                        binding.lvNoData.visibility = View.GONE
                        binding.absentLayout.visibility = View.VISIBLE
                        binding.presentLayout.visibility = View.VISIBLE
                        binding.attendanceCalendarViewLayout.visibility = View.VISIBLE
                    }

                    Log.e("TAG", "setupObserver: attendanceList----$attendanceList")
                    Log.e("TAG", "setupObserver: attendancePresentList----$attendancePresentList")
                    Log.e("TAG", "setupObserver: attendanceLeaveList----$attendanceLeaveList")

                    getEmptyEvents()

                    binding.attendanceCalendarView.clearSelection()
                    binding.attendanceCalendarView.removeDecorators()
                    binding.attendanceCalendarView.removeDecorator(RemoveDecorator(completedDates))
                    binding.attendanceCalendarView.removeDecorator(RemoveDecorator(pauseDates))
                    binding.attendanceCalendarView.invalidateDecorators()

                    binding.attendanceCalendarView.addDecorators(
                        EventDecorator(activity, getAbsentEvents()),
                        MySelectorDecorator(activity),
                        OneDayDecorator(),
                        EventAttendanceDecorator(activity, getPresentEvents())
                    )
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

    private fun getEmptyEvents() {
        completedDates.clear()
        pauseDates.clear()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        attendanceLeaveList.forEach {
            val localDate = LocalDate.parse(it.date, formatter)
            val day = CalendarDay.from(localDate)
            completedDates.remove(day)
            binding.attendanceCalendarView.removeDecorator(RemoveDecorator(completedDates))
            binding.attendanceCalendarView.invalidateDecorators()

        }

        attendancePresentList.forEach {
            val localDate = LocalDate.parse(it.date, formatter)
            val day = CalendarDay.from(localDate)
            pauseDates.remove(day)
            binding.attendanceCalendarView.removeDecorator(RemoveDecorator(completedDates))
            binding.attendanceCalendarView.invalidateDecorators()

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
        if (item1 != null) item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        if (item2 != null) item2.isVisible = true

    }

}
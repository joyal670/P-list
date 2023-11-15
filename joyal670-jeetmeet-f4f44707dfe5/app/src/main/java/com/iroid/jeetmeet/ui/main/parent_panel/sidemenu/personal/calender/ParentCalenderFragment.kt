package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.calender

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentCalenderBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.calender.ParentCalenderData
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentCalenderViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentStudentsListViewModel
import com.iroid.jeetmeet.utils.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class ParentCalenderFragment : BaseFragment() {

    private lateinit var binding: FragmentParentCalenderBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentCalenderViewModel: ParentCalenderViewModel
    private lateinit var parentStudentsListViewModel: ParentStudentsListViewModel
    private var studentList = ArrayList<ParentStudentsListData>()
    private var assignmentList = ArrayList<ParentCalenderData>()

    var studentNameList = ArrayList<String>()
    var studentIdList = ArrayList<Int>()

    private var selectedId = 0
    private var selectedName: String = ""

    var calenderType = arrayListOf<String>()
    val completedDates = ArrayList<CalendarDay>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentCalenderBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.calender))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        val instance = LocalDate.now()
        binding.studentCalender.setSelectedDate(instance)
        binding.rvstudentAssignment.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* parent calender */
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

        parentCalenderViewModel = ParentCalenderViewModel()
        parentCalenderViewModel.parentCalender(selectedId)
        parentCalenderViewModel.parentCalenderData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    assignmentList.clear()
                    assignmentList.addAll(it.data!!.data)

                    binding.rvstudentAssignment.adapter = ParentAssignmentAdapter(assignmentList)

                    getEmptyEvents()

                    if (assignmentList.size == 0) {
                        binding.studentCalender.visibility = View.GONE
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    } else {
                        binding.studentCalender.visibility = View.VISIBLE
                        binding.rvstudentAssignment.visibility = View.VISIBLE
                    }

                    binding.studentCalender.clearSelection()
                    binding.studentCalender.removeDecorators()
                    binding.studentCalender.removeDecorator(RemoveDecorator(completedDates))
                    binding.studentCalender.invalidateDecorators()

                    binding.studentCalender.addDecorators(
                        EventDecorator(activity, getEvents()),
                        MySelectorDecorator(activity),
                        OneDayDecorator()
                    )


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
            }
        })
    }

    private fun getEvents(): ArrayList<CalendarDay> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        assignmentList.forEach {
            val localDate = LocalDate.parse(it.deadline_date.take(10), formatter)
            val day = CalendarDay.from(localDate)
            completedDates.add(day)
        }
        return completedDates
    }

    private fun getEmptyEvents() {
        completedDates.clear()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        assignmentList.forEach {
            val localDate = LocalDate.parse(it.deadline_date.take(10), formatter)
            val day = CalendarDay.from(localDate)
            completedDates.remove(day)
            binding.studentCalender.removeDecorator(RemoveDecorator(completedDates))
            binding.studentCalender.invalidateDecorators()

        }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

}
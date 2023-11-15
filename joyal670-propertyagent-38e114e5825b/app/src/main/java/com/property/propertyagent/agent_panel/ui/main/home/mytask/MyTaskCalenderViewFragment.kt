package com.property.propertyagent.agent_panel.ui.main.home.mytask

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.mytask.viewmodel.MyTaskCalenderViewViewModel
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_calender_task_count.AgentTask
import com.property.propertyagent.modal.agent.agent_calender_task_list.Task
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import kotlinx.android.synthetic.main.fragment_mytask_calenderview.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MyTaskCalenderViewFragment : BaseFragment(), OnDateSelectedListener {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var myTaskCalenderViewViewModel: MyTaskCalenderViewViewModel
    private var calenderTaskList: ArrayList<Task>? = null
    private var agentTaskCount: ArrayList<AgentTask>? = null
    private lateinit var myTaskCalenderAdapter: MyTaskCalenderAdapter
    private var passedPosition: Int = -1
    private var passedTaskId = ""
    private lateinit var widget: MaterialCalendarView
    private var checkTime = false
    private var datePassed: String = ""
    private var dialog: Dialog? = null
    private var singleEvent = ArrayList<CalendarDay>()
    private var multipleEvent = ArrayList<CalendarDay>()

    private var isLoadingMain: Boolean = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_mytask_calenderview, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_task))

        calenderTaskList = ArrayList()
        agentTaskCount = ArrayList()

        singleEvent = ArrayList()
        multipleEvent = ArrayList()
        setCalenderWithEventsCount(agentTaskCount!!)

        myTaskCalenderViewViewModel.agentCalenderTaskCountDetails(requireContext().getCurrentDate())
    }

    private fun setupCalenderRecyclerView() {
        mytaskcalenderfrgRecycerview.layoutManager = LinearLayoutManager(context)
        myTaskCalenderAdapter = MyTaskCalenderAdapter(calenderTaskList!!)
        { pos1, taskId1, complete -> updateCompletedStatus(pos1, taskId1, complete) }
        mytaskcalenderfrgRecycerview.adapter = myTaskCalenderAdapter
    }

    private fun updateCompletedStatus(
        pos1: Int,
        taskId1: String,
        complete: Int,
    ) {
        passedPosition = pos1
        passedTaskId = taskId1
        if (complete == 0) {
            myTaskCalenderViewViewModel.fetchAgentUpdateTaskStatus(taskId1)
        } else {
            myTaskCalenderViewViewModel.fetchAgentUpdateCompletedTaskStatus(taskId1)
        }
    }

    override fun setupUI() {

    }

    private fun setCalenderWithEventsCount(agentTaskCount: ArrayList<AgentTask>) {
        widget = view?.findViewById(R.id.calendarView) as MaterialCalendarView
        widget.setOnDateChangedListener(this)
        val instance = LocalDate.now()
        widget.setSelectedDate(instance)
        widget.isDynamicHeightEnabled = true

        widget.addDecorators(
            EventDecorator(activity, getEvents(agentTaskCount)),
            MySelectorDecorator(activity),
            OneDayDecorator(),
            EventTwoDecorator(activity, getMultipleEvents(agentTaskCount))
        )
    }

    override fun setupViewModel() {
        myTaskCalenderViewViewModel = MyTaskCalenderViewViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        myTaskCalenderViewViewModel.getAgentAddTaskResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        Log.e("response--details", Gson().toJson(it))
                        AppPreferences.reload_my_task_pending_list = true
                        dialog!!.dismiss()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        initData()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }

        myTaskCalenderViewViewModel.getAgentCalenderTaskCountResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        myTaskCalenderViewViewModel.fetchAgentCalenderTaskList(requireContext().getCurrentDate())
                        Log.e("response--details", Gson().toJson(it))
                        if (it.data != null) {
                            if (!(it.data.agent_task.isNullOrEmpty())) {
                                agentTaskCount = it.data.agent_task as ArrayList<AgentTask>
                                setCalenderWithEventsCount(agentTaskCount!!)
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }

        myTaskCalenderViewViewModel.getAgentCalenderTaskListResponse().observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    if (!isLoadingMain) {
                        showProgress()
                    }
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    isLoadingMain = false
                    calenderTaskList!!.clear()
                    Log.e("response--details", Gson().toJson(it))
                    if (it.data != null) {
                        if (!it.data.task_data.equals(null)) {
                            if (!(it.data.task_data.task.isNullOrEmpty())) {
                                calenderTaskList = it.data.task_data.task as ArrayList<Task>
                                setupCalenderRecyclerView()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
            }
        }
        myTaskCalenderViewViewModel.getAgentUpdateCompletedTaskStatusResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        calenderTaskList!![passedPosition].completed = 0
                        myTaskCalenderAdapter.notifyDataSetChanged()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        AppPreferences.reload_my_task_pending_list = true
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }
        myTaskCalenderViewViewModel.getAgentUpdateTaskStatusResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        calenderTaskList!![passedPosition].completed = 1
                        myTaskCalenderAdapter.notifyDataSetChanged()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        AppPreferences.reload_my_task_pending_list = true
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }
    }

    override fun onClicks() {

    }

    private fun getEvents(agentTaskCount: ArrayList<AgentTask>): ArrayList<CalendarDay> {
        for (i in 0 until agentTaskCount.size) {
            if (agentTaskCount[i].task_count == 1) {
                val temp: LocalDate = LocalDate.parse(agentTaskCount[i].task_date)
                val day = CalendarDay.from(temp)
                singleEvent.add(day)
            }
        }
        Log.e("final single event", Gson().toJson(singleEvent))

        return singleEvent
    }

    private fun getMultipleEvents(agentTaskCount1: ArrayList<AgentTask>): ArrayList<CalendarDay> {
        for (i in 0 until agentTaskCount1.size) {
            if (agentTaskCount1[i].task_count != 1) {
                val temp: LocalDate = LocalDate.parse(agentTaskCount1[i].task_date)
                val day = CalendarDay.from(temp)
                multipleEvent.add(day)
            }
        }
        return multipleEvent
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean,
    ) {

        tvTapOneDay.visibility = View.GONE
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
        val formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        dialog = Dialog(requireContext())
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_task_layout)

        val etTitleDialog = dialog!!.findViewById(R.id.etTitleDialog) as EditText
        val yesBtn = dialog!!.findViewById(R.id.yesBtn) as Button
        val fragmentCalenderCurrentDate =
            dialog!!.findViewById(R.id.fragment_calender_current_date) as TextView
        val fragmentCalenderTime = dialog!!.findViewById(R.id.tvTime) as TextView
        fragmentCalenderTime.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText(requireContext().getString(R.string.tvTime))
                    .setHour(12)
                    .setMinute(10)
                    .build()
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                // call back code
                var newHour: String = picker.hour.toString()
                var newMinute: String = picker.minute.toString()
                if (newHour.length == 1) {
                    newHour = "0$newHour"
                }
                if (newMinute.length == 1) {
                    newMinute = "0$newMinute"
                }
                Log.e("time", "$newHour:$newMinute")
                checkTime = true
                fragmentCalenderTime.text = "$newHour:$newMinute"
                fragmentCalenderTime.setTextColor(Color.BLACK)
            }
        }

        datePassed = formatter2.format(date.date)
        fragmentCalenderCurrentDate.text = formatter.format(date.date)

        yesBtn.setOnClickListener {
            when {
                etTitleDialog.text.trim().toString().isEmpty() -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.title_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                }
                fragmentCalenderTime.text.trim().toString().isEmpty() -> {
                    Toaster.showToast(
                        requireContext(), getString(R.string.time_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                }
                else -> {
                    myTaskCalenderViewViewModel.agentAddTaskDetails(
                        etTitleDialog.text.trim().toString(), datePassed,
                        fragmentCalenderTime.text.trim().toString()
                    )
                }
            }
        }
        dialog!!.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog!!.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window?.attributes = layoutParams
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            myTaskCalenderViewViewModel.agentCalenderTaskCountDetails(requireContext().getCurrentDate())
        }
    }
}



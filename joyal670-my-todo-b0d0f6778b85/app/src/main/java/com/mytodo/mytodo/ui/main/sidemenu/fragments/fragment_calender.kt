package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentCalenderBinding
import com.mytodo.mytodo.ui.main.sidemenu.adapter.studentAssignmentAdapter
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.EventDecorator
import com.mytodo.mytodo.utils.MySelectorDecorator
import com.mytodo.mytodo.utils.OneDayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import io.realm.Realm
import io.realm.RealmResults
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import kotlin.collections.ArrayList


class fragment_calender : BaseFragment()
{
    private lateinit var binding: FragmentCalenderBinding
    val completedDates = ArrayList<CalendarDay>()

    private var dataModel: TaskModel? = null
    var taskList: RealmResults<TaskModel>? = null
    private var realm: Realm? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalenderBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        realm = Realm.getDefaultInstance()
        dataModel = TaskModel()
        taskList = realm!!.where(TaskModel::class.java).findAll()

        val instance = LocalDate.now()
        binding.studentcalendarView.setSelectedDate(instance)
        binding.studentcalendarView.addDecorators(EventDecorator(activity, getEvents()), MySelectorDecorator(activity),
            OneDayDecorator()
        )

        if (taskList!!.isEmpty() || taskList!!.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvstudentAssignment.visibility = View.GONE
        } else {
            binding.lvNoData.visibility = View.GONE
            binding.rvstudentAssignment.visibility = View.VISIBLE
        }

        binding.rvstudentAssignment.layoutManager = LinearLayoutManager(context)
        binding.rvstudentAssignment.adapter = studentAssignmentAdapter(taskList!!)
    }

    override fun setupUI() {
    }

    private fun getEvents(): ArrayList<CalendarDay> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        taskList!!.forEach {
            val temp1 = formatter.parse(it.startDate)
            val localDate = LocalDate.parse(it.startDate, formatter)
            val day = CalendarDay.from(localDate)
            completedDates.add(day)
        }
        /*for (i in 0..29) {
            val day = CalendarDay.from(temp)
            completedDates.add(day)
            temp = temp.plusDays(5)
        }*/
        Log.e("TAG1234567889", "initData: $completedDates")
        return completedDates
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

}
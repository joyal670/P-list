package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentNotificationBinding
import com.mytodo.mytodo.ui.main.sidemenu.adapter.NotificationAdapter
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.Realm
import io.realm.RealmResults
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class fragment_notification : BaseFragment() {
    private lateinit var binding: FragmentNotificationBinding
    private var dataModel: TaskModel? = null
    var taskList: RealmResults<TaskModel>? = null
    private var realm: Realm? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        binding.swipeRefreshLayout.setRefreshing(false)

        realm = Realm.getDefaultInstance()
        dataModel = TaskModel()

        val sdf = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val sdf1 = SimpleDateFormat("h:ma", Locale.getDefault())

        try {

            val currentDateString = sdf.format(Date())
            val currentTimeString = sdf1.format(Date())

            Log.e("TAG", "initData: currentTimeString-----"+currentTimeString )

            val inputFormat: DateFormat = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
            val outputFormat: DateFormat = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
            val startDateStr = currentDateString
            val date: Date?
            try {
                date = inputFormat.parse(startDateStr)
                val startDateStrNewFormat: String = outputFormat.format(date)

                Log.e("TAG", "startDateStrNewFormat----- " + startDateStrNewFormat)

                taskList = realm!!.where(TaskModel::class.java).equalTo("endDate", startDateStrNewFormat).findAll()


                Log.e("TAG", "taskList -----" + taskList)

            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e("TAG", "Exception -----" + e)
            }


        } catch (e: Exception) {
            Log.e("TAG", "Exception: $e")
        }

        if (taskList!!.isEmpty() || taskList!!.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvNotification.visibility = View.GONE
        } else {
            binding.lvNoData.visibility = View.GONE
            binding.rvNotification.visibility = View.VISIBLE
        }

        binding.rvNotification.layoutManager = LinearLayoutManager(context)
        binding.rvNotification.adapter = NotificationAdapter(taskList!!)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            initData()
        }
    }

}
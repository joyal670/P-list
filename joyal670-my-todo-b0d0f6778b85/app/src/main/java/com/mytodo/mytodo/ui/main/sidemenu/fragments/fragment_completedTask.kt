package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentCompletedTaskBinding
import com.mytodo.mytodo.ui.main.sidemenu.adapter.MyTaskCompletedAdapter
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.Toaster
import io.realm.Realm
import io.realm.RealmResults


class fragment_completedTask : BaseFragment() {
    private lateinit var binding: FragmentCompletedTaskBinding
    private var dataModel: TaskModel? = null
    var completedList: RealmResults<TaskModel>? = null
    private var realm: Realm? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedTaskBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        binding.swipeRefreshLayout.setRefreshing(false)

        realm = Realm.getDefaultInstance()
        dataModel = TaskModel()
        completedList = realm!!.where(TaskModel::class.java).equalTo("isCompleted", true).findAll()

        if (completedList!!.isEmpty() || completedList!!.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvTask1.visibility = View.GONE
        } else {
            binding.lvNoData.visibility = View.GONE
            binding.rvTask1.visibility = View.VISIBLE
        }

        binding.rvTask1.layoutManager = LinearLayoutManager(context)
        binding.rvTask1.adapter = MyTaskCompletedAdapter(completedList!!) { deleteTask(it) }
    }

    private fun deleteTask(it: Int) {
        try {

            realm!!.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    val dataModel =
                        realm.where(TaskModel::class.java).equalTo("id", it).findFirst()!!
                            .deleteFromRealm()
                }
            })
            Toaster.showToast(
                requireContext(),
                "Task Deleted",
                Toaster.State.SUCCESS,
                Toast.LENGTH_SHORT
            )

            initData()

        } catch (e: Exception) {
            Log.e("Status", "Something went Wrong !!!")
            Toaster.showToast(
                requireContext(),
                "Something went wrong",
                Toaster.State.ERROR,
                Toast.LENGTH_SHORT
            )
        }

    }

    override fun setupUI() {
        binding.tvView1.paintFlags = binding.tvView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
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


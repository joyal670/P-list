package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentMytaskBinding
import com.mytodo.mytodo.dialog.MyTaskTutorialDialog
import com.mytodo.mytodo.ui.main.sidemenu.activity.MyTaskActivity
import com.mytodo.mytodo.ui.main.sidemenu.adapter.MyTaskPendingAdapter
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.AppPreferences.prefIsTaskTutorial
import com.mytodo.mytodo.utils.Constants
import com.mytodo.mytodo.utils.EnumFromPage
import com.mytodo.mytodo.utils.Toaster
import io.realm.Realm
import io.realm.RealmResults


class fragment_mytask : BaseFragment() {
    private lateinit var binding: FragmentMytaskBinding
    private var dataModel: TaskModel? = null
    var unCompletedList: RealmResults<TaskModel>? = null
    private var realm: Realm? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMytaskBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        binding.swipeRefreshLayout.setRefreshing(false)

        if (!prefIsTaskTutorial) {
            val dialog = MyTaskTutorialDialog()
            dialog.show(parentFragmentManager, "TAG")
            prefIsTaskTutorial = true
        }

        realm = Realm.getDefaultInstance()
        dataModel = TaskModel()
        unCompletedList =
            realm!!.where(TaskModel::class.java).equalTo("isCompleted", false).findAll()

        if (unCompletedList!!.isEmpty() || unCompletedList!!.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvTask1.visibility = View.GONE
        } else {
            binding.lvNoData.visibility = View.GONE
            binding.rvTask1.visibility = View.VISIBLE
        }

        binding.rvTask1.layoutManager = LinearLayoutManager(context)
        binding.rvTask1.adapter =
            MyTaskPendingAdapter(unCompletedList, { deleteTask(it) }, { completeTask(it) })
    }

    private fun completeTask(i: Int) {
        try {
            realm!!.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    val dataModel = realm.where(TaskModel::class.java).equalTo("id", i).findFirst()
                    dataModel!!.isCompleted = true
                }
            })
            Toaster.showToast(
                requireContext(),
                "Task Completed",
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

    private fun deleteTask(i: Int) {
        try {
            realm!!.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    val dataModel =
                        realm.where(TaskModel::class.java).equalTo("id", i).findFirst()!!
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
        binding.fbCompleted.setOnClickListener {
            val intent = Intent(requireContext(), MyTaskActivity::class.java)
            intent.putExtra(Constants.TASKTYPE, EnumFromPage.COMPLETED_TASK.name)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            initData()
        }


    }

}
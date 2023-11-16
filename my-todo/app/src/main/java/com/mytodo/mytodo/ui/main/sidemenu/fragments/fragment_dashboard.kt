package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.flexbox.*
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentDashboardBinding
import com.mytodo.mytodo.dialog.DashboardTutorialDialog
import com.mytodo.mytodo.ui.main.sidemenu.activity.MyTaskActivity
import com.mytodo.mytodo.ui.main.sidemenu.adapter.DashBoardAdapter
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.AppPreferences.prefIsDashboardTutorial
import com.mytodo.mytodo.utils.Constants
import com.mytodo.mytodo.utils.EnumFromPage
import com.mytodo.mytodo.utils.Toaster
import io.realm.Realm
import io.realm.RealmResults


class fragment_dashboard : BaseFragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var dataModel : TaskModel? = null
    var taskList: RealmResults<TaskModel>? = null
    private var realm: Realm? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        binding.swipeRefreshLayout.setRefreshing(false)

        if(!prefIsDashboardTutorial)
        {
            val dialog = DashboardTutorialDialog()
            dialog.show(parentFragmentManager, "TAG")
            prefIsDashboardTutorial = true
        }

        try {
            realm = Realm.getDefaultInstance()
            dataModel = TaskModel()
            taskList = realm!!.where(TaskModel::class.java).findAll()

            if (taskList!!.isEmpty() || taskList!!.size == 0)
            {
                binding.lvNoData.visibility= View.VISIBLE
                binding.rvDashboard.visibility = View.GONE
            }
            else
            {
                binding.lvNoData.visibility= View.GONE
                binding.rvDashboard.visibility = View.VISIBLE
            }

            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.flexWrap = FlexWrap.WRAP
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.STRETCH

            Log.e("TAG", "initData: $taskList")

            binding.rvDashboard.layoutManager = layoutManager
            binding.rvDashboard.adapter = DashBoardAdapter(taskList)


        }catch (e: Exception) {
            Log.e("TAG", "initData: $e")
            Toaster.showToast(requireContext(),"Something went wrong", Toaster.State.ERROR, Toast.LENGTH_SHORT)
        }

    }


    override fun setupUI() {
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        binding.fbAddTask.setOnClickListener {
            val intent = Intent(requireContext(), MyTaskActivity::class.java)
            intent.putExtra(Constants.TASKTYPE, EnumFromPage.ADD_TASK.name)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

}
package com.property.propertyagent.agent_panel.ui.main.home.mytask

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.mytask.viewmodel.MyTaskViewModel
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_pending_task_list.Task
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_mytask.*

class MyTaskFragment : BaseFragment() {
    private lateinit var myTaskViewModel: MyTaskViewModel
    private var pendingTaskList: ArrayList<Task>? = null
    private var fullPendingTaskList: ArrayList<Task>? = null

    private var completedTaskList: ArrayList<Task>? = null
    private var fullCompletedTaskList: ArrayList<Task>? = null

    private lateinit var myTaskPendingAdapter: MyTaskPendingAdapter
    private lateinit var myTaskCompletedAdapter: MyTaskCompletedAdapter

    private var passedPosition: Int = -1
    private var passedTaskId = ""

    private var passedPositionCompleted: Int = -1
    private var passedTaskIdCompleted = ""

    private var isLoadingMain: Boolean = false
    private var isPendingEmpty: Boolean = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_mytask, container, false)
    }

    override fun initData() {
        mytask_frag_viewAllTv1.paintFlags =
            mytask_frag_viewAllTv1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        mytask_frag_viewAllTv2.paintFlags =
            mytask_frag_viewAllTv2.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        pendingTaskList = ArrayList()
        fullPendingTaskList = ArrayList()

        completedTaskList = ArrayList()
        fullCompletedTaskList = ArrayList()

        myTaskViewModel.fetchAgentPendingTaskList()
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.reload_my_task_pending_list) {
            AppPreferences.reload_my_task_pending_list = false
            myTaskViewModel.fetchAgentPendingTaskList()
        } else if (isConnectionRestored) {
            isConnectionRestored = false
            myTaskViewModel.fetchAgentPendingTaskList()
        }
    }

    private fun setPendingRecyclerView() {
        mytaskfrgRecycerview.layoutManager = LinearLayoutManager(context)
        myTaskPendingAdapter =
            MyTaskPendingAdapter(pendingTaskList!!) { pos, taskId -> updateStatus(pos, taskId) }
        mytaskfrgRecycerview.adapter = myTaskPendingAdapter

        if(pendingTaskList!!.size == 0){
            mytaskfrgRecycerview.visibility = View.GONE
            animationView1.visibility = View.VISIBLE
            noTask1.visibility = View.VISIBLE
        }else{
            mytaskfrgRecycerview.visibility = View.VISIBLE
            animationView1.visibility = View.GONE
            noTask1.visibility = View.GONE
        }
    }

    private fun updateStatus(pos: Int, taskId: String) {
        Log.e("pos first", pos.toString())
        passedPosition = pos
        passedTaskId = taskId
        myTaskViewModel.fetchAgentUpdateTaskStatus(taskId)
    }

    private fun setCompletedRecyclerView() {
        mycompletedtaskfrgRecycerview.layoutManager = LinearLayoutManager(context)
        myTaskCompletedAdapter = MyTaskCompletedAdapter(completedTaskList!!) { pos1, taskId1 ->
            updateCompletedStatus(
                pos1,
                taskId1
            )
        }
        mycompletedtaskfrgRecycerview.adapter = myTaskCompletedAdapter

        if(fullCompletedTaskList!!.size == 0){
            mycompletedtaskfrgRecycerview.visibility = View.GONE
            animationView2.visibility = View.VISIBLE
            noTask2.visibility = View.VISIBLE
        }else{
            mycompletedtaskfrgRecycerview.visibility = View.VISIBLE
            animationView2.visibility = View.GONE
            noTask2.visibility = View.GONE
        }
    }

    private fun updateCompletedStatus(pos1: Int, taskId1: String) {
        Log.e("pos1 first", pos1.toString())
        passedPositionCompleted = pos1
        passedTaskIdCompleted = taskId1
        myTaskViewModel.fetchAgentUpdateCompletedTaskStatus(taskId1)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        myTaskViewModel = MyTaskViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        myTaskViewModel.getAgentPendingTaskListResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        myTaskViewModel.fetchAgentCompletedTaskList()
                        pendingTaskList!!.clear()
                        fullPendingTaskList!!.clear()
                        Log.e("response--details", Gson().toJson(it))
                        if (it.data != null) {
                            if (!it.data.task_data.equals(null)) {
                                if (!(it.data.task_data.task.isNullOrEmpty())) {
                                    fullPendingTaskList = it.data.task_data.task as ArrayList<Task>
                                    if (fullPendingTaskList!!.size > 3) {
                                        for (i in 0 until 3) {
                                            pendingTaskList!!.add(fullPendingTaskList!![i])
                                        }
                                    } else {
                                        pendingTaskList = fullPendingTaskList
                                    }

                                } else {
                                    isPendingEmpty = true
                                }
                            }
                        }
                        setPendingRecyclerView()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
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

        myTaskViewModel.getAgentCompletedTaskListResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        completedTaskList!!.clear()
                        fullCompletedTaskList!!.clear()
                        Log.e("response--details", Gson().toJson(it))
                        if (it.data != null) {
                            if (!it.data.task_data.equals(null)) {
                                if (!(it.data.task_data.task.isNullOrEmpty())) {
                                    fullCompletedTaskList =
                                        it.data.task_data.task as ArrayList<Task>
                                    if (fullCompletedTaskList!!.size > 3) {
                                        for (i in 0 until 3) {
                                            completedTaskList!!.add(fullCompletedTaskList!![i])
                                        }
                                    } else {
                                        completedTaskList = fullCompletedTaskList
                                    }

                                    container.isVisible = true
                                } else {

                                   /* if (isLoadingMain) {
                                        if (isPendingEmpty) {
                                            llEmptyData.isVisible = true
                                            container.isVisible = false
                                        } else {
                                            container.isVisible = true
                                        }
                                    }*/
                                }
                            }
                        }
                        setCompletedRecyclerView()
                        isLoadingMain = false
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
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

        myTaskViewModel.getAgentUpdateTaskStatusResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        if (fullPendingTaskList!!.isNotEmpty()) {
                            for (k in 0 until fullPendingTaskList!!.size) {
                                if (fullPendingTaskList!![k].id == passedTaskId.toInt()) {
                                    fullPendingTaskList!!.removeAt(k)
                                    break
                                }
                            }
                        }
                        if (pendingTaskList!!.isNotEmpty()) {
                            for (l in 0 until pendingTaskList!!.size) {
                                if (pendingTaskList!![l].id == passedTaskId.toInt()) {
                                    pendingTaskList!!.removeAt(l)
                                    break
                                }
                            }
                        }
                        if (pendingTaskList!!.size < 3) {
                            for (i in 0 until fullPendingTaskList!!.size) {
                                for (j in 0 until pendingTaskList!!.size) {
                                    if (fullPendingTaskList!![i].id != pendingTaskList!![j].id) {
                                        if (!(pendingTaskList!!.contains(fullPendingTaskList!![i]))) {
                                            if (pendingTaskList!!.size < 3) {
                                                pendingTaskList!!.add(fullPendingTaskList!![i])
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        myTaskPendingAdapter.notifyDataSetChanged()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        initData()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
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

        myTaskViewModel.getAgentUpdateCompletedTaskStatusResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {

                        if (fullCompletedTaskList!!.isNotEmpty()) {
                            for (k in 0 until fullCompletedTaskList!!.size) {
                                if (fullCompletedTaskList!![k].id == passedTaskIdCompleted.toInt()) {
                                    fullCompletedTaskList!!.removeAt(k)
                                    break
                                }
                            }
                        }
                        if (completedTaskList!!.isNotEmpty()) {
                            for (l in 0 until completedTaskList!!.size) {
                                if (completedTaskList!![l].id == passedTaskIdCompleted.toInt()) {
                                    completedTaskList!!.removeAt(l)
                                    break
                                }
                            }
                        }

                        if (completedTaskList!!.size < 3) {
                            for (i in 0 until fullCompletedTaskList!!.size) {
                                for (j in 0 until completedTaskList!!.size) {
                                    if (fullCompletedTaskList!![i].id != completedTaskList!![j].id) {
                                        if (!(completedTaskList!!.contains(fullCompletedTaskList!![i]))) {
                                            if (completedTaskList!!.size < 3) {
                                                pendingTaskList!!.add(fullCompletedTaskList!![i])
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        myTaskCompletedAdapter.notifyDataSetChanged()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        initData()

                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
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
        mytask_frag_viewAllTv1.setOnClickListener {
            pendingTaskList = fullPendingTaskList
            setPendingRecyclerView()
        }
        mytask_frag_viewAllTv2.setOnClickListener {
            completedTaskList = fullCompletedTaskList
            setCompletedRecyclerView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item2: MenuItem = menu.findItem(R.id.customtoolbar_notification)
        item2.isVisible = false
    }


}
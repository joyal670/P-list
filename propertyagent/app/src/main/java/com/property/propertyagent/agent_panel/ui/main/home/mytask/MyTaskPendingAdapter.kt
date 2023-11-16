package com.property.propertyagent.agent_panel.ui.main.home.mytask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_pending_task_list.Task
import kotlinx.android.synthetic.main.recycle_mytask_list_item.view.*

class MyTaskPendingAdapter(
    private var pendingTaskList: List<Task>,
    private val functionUpdateStatus: (Int, String) -> Unit,
) : RecyclerView.Adapter<MyTaskPendingAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_mytask_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return pendingTaskList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        if (pendingTaskList[position].completed != 1) {
            holder.itemView.MyTaskPendingAdapter_taskName.text = pendingTaskList[position].title
            holder.itemView.cbPendingStatusTick.isChecked = false
            holder.itemView.tvDate.text = pendingTaskList[position].task_date
            holder.itemView.tvTime.text = pendingTaskList[position].task_time

            holder.itemView.cbPendingStatusTick.setOnClickListener {
                holder.itemView.cbPendingStatusTick.isChecked = true
                functionUpdateStatus.invoke(position, pendingTaskList[position].id.toString())
            }
        }
    }
}
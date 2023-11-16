package com.property.propertyagent.agent_panel.ui.main.home.mytask

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_calender_task_list.Task
import kotlinx.android.synthetic.main.recycle_mytask_completed_list_item.view.*

class MyTaskCalenderAdapter(
    private var calenderTaskList: List<Task>,
    private val functionUpdateStatus: (Int, String, Int) -> Unit,
) : RecyclerView.Adapter<MyTaskCalenderAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_mytask_completed_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return calenderTaskList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        if (calenderTaskList[position].completed == 1) {
            holder.itemView.MyTaskCompletedAdapter_taskName.paintFlags =
                holder.itemView.MyTaskCompletedAdapter_taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskDate.paintFlags =
                holder.itemView.MyTaskCompletedAdapter_taskDate.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskLine.paintFlags =
                holder.itemView.MyTaskCompletedAdapter_taskLine.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.MyTaskCompletedAdapter_tasktime.paintFlags =
                holder.itemView.MyTaskCompletedAdapter_tasktime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskDesc.paintFlags =
                holder.itemView.MyTaskCompletedAdapter_taskDesc.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.itemView.MyTaskCompletedAdapter_taskName.paintFlags = Paint.ANTI_ALIAS_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskDate.paintFlags = Paint.ANTI_ALIAS_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskLine.paintFlags = Paint.ANTI_ALIAS_FLAG
            holder.itemView.MyTaskCompletedAdapter_tasktime.paintFlags = Paint.ANTI_ALIAS_FLAG
            holder.itemView.MyTaskCompletedAdapter_taskDesc.paintFlags = Paint.ANTI_ALIAS_FLAG
        }
        holder.itemView.MyTaskCompletedAdapter_taskName.text = calenderTaskList[position].title
        holder.itemView.cbCompletedStatusTick.isChecked = calenderTaskList[position].completed == 1

        holder.itemView.cbCompletedStatusTick.setOnClickListener {
            holder.itemView.cbCompletedStatusTick.isChecked =
                !holder.itemView.cbCompletedStatusTick.isChecked
            functionUpdateStatus.invoke(
                position, calenderTaskList[position].id.toString(),
                calenderTaskList[position].completed
            )
        }
    }
}
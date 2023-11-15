package com.property.propertyagent.agent_panel.ui.main.home.mytask

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_pending_task_list.Task
import kotlinx.android.synthetic.main.recycle_mytask_completed_list_item.view.*

class MyTaskCompletedAdapter(
    private var completedTaskList : List<Task> ,
    private val functionUpdateCompletedStatus : (Int , String) -> Unit ,
) : RecyclerView.Adapter<MyTaskCompletedAdapter.ViewHold>() {
    private var context : Context? = null

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_mytask_completed_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return completedTaskList.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
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

        if (completedTaskList[position].completed == 1) {
            holder.itemView.MyTaskCompletedAdapter_taskName.text = completedTaskList[position].title
            holder.itemView.cbCompletedStatusTick.isChecked = true
            holder.itemView.MyTaskCompletedAdapter_taskDate.text =
                completedTaskList[position].task_date
            holder.itemView.MyTaskCompletedAdapter_tasktime.text =
                completedTaskList[position].task_time

            holder.itemView.cbCompletedStatusTick.setOnClickListener {
                holder.itemView.cbCompletedStatusTick.isChecked = false
                functionUpdateCompletedStatus.invoke(position ,
                    completedTaskList[position].id.toString())
            }
        }
    }
}
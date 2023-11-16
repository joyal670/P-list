package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytodo.mytodo.databinding.RecycleStudentAssignmentListItemBinding
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.RealmResults


class studentAssignmentAdapter(private var taskList: RealmResults<TaskModel>) : RecyclerView.Adapter<studentAssignmentAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(var binding : RecycleStudentAssignmentListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = RecycleStudentAssignmentListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int
    {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.binding.timeBtn.text = taskList[position]!!.startDate.toString()
        holder.binding.tvStudentAssignmentDesc.text = taskList[position]!!.title
        holder.binding.tvEndDate.text = "End Date : " +taskList[position]!!.endDate
    }
}

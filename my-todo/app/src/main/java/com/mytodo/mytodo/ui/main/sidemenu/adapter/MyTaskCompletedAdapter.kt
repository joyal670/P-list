package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytodo.mytodo.databinding.RecycleMytaskCompletedListItemBinding
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.RealmResults


class MyTaskCompletedAdapter(private var completedList: RealmResults<TaskModel>,  private val deleteTask: (Int) -> Unit) : RecyclerView.Adapter<MyTaskCompletedAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(var binding : RecycleMytaskCompletedListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = RecycleMytaskCompletedListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int
    {
        return completedList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.binding.MyTaskCompletedAdapterTaskName.paintFlags =
            holder.binding.MyTaskCompletedAdapterTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.MyTaskCompletedAdapterTaskDate.paintFlags =
            holder.binding.MyTaskCompletedAdapterTaskDate.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.MyTaskCompletedAdapterTaskLine.paintFlags =
            holder.binding.MyTaskCompletedAdapterTaskLine.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.MyTaskCompletedAdapterTasktime.paintFlags =
            holder.binding.MyTaskCompletedAdapterTasktime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.MyTaskCompletedAdapterTaskDesc.paintFlags =
            holder.binding.MyTaskCompletedAdapterTaskDesc.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.binding.MyTaskCompletedAdapterTaskName.text = completedList[position]!!.title
        holder.binding.MyTaskCompletedAdapterTaskDate.text = completedList[position]!!.endDate.toString()
        holder.binding.MyTaskCompletedAdapterTasktime.text = completedList[position]!!.endTime
        holder.binding.MyTaskCompletedAdapterTaskDesc.text = completedList[position]!!.description

        holder.binding.deleteLayout.setOnClickListener {
            deleteTask.invoke(completedList[position]!!.id)
        }

    }


}

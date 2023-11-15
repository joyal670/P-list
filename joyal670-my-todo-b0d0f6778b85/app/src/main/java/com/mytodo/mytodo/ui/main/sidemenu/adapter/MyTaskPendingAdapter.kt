package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytodo.mytodo.databinding.RecycleMytaskListItemBinding
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.RealmResults


class MyTaskPendingAdapter(
    private var unCompletedList: RealmResults<TaskModel>?,
    private val deleteTask: (Int) -> Unit,
    private val completeTask: (Int) -> Unit
) : RecyclerView.Adapter<MyTaskPendingAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleMytaskListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleMytaskListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return unCompletedList!!.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.binding.taskName.text = unCompletedList!![position]!!.title
        holder.binding.startDate.text = unCompletedList!![position]!!.startDate.toString()
        holder.binding.startTime.text = unCompletedList!![position]!!.startTime
        holder.binding.tvDesc.text = unCompletedList!![position]!!.description

        holder.binding.deleteLayout.setOnClickListener {
            deleteTask.invoke(unCompletedList!![position]!!.id)
        }

        holder.binding.completeLayout.setOnClickListener {
            completeTask.invoke(unCompletedList!![position]!!.id)
        }
    }
}

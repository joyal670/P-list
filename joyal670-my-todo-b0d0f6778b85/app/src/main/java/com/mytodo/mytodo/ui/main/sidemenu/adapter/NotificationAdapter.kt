package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytodo.mytodo.databinding.RecycleNotificationListItemBinding
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import io.realm.RealmResults


class NotificationAdapter(private var completedList: RealmResults<TaskModel>) : RecyclerView.Adapter<NotificationAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(var binding : RecycleNotificationListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = RecycleNotificationListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int
    {
        return completedList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.binding.tvNotificationTitle.text = completedList[position]!!.title
        holder.binding.tvNotificationDate.text = completedList[position]!!.endDate.toString()
        holder.binding.tvNotificationMessage.text = completedList[position]!!.description

    }
}
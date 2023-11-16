package com.property.propertyagent.agent_panel.ui.main.sidemenu.notification.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_notification_list.Notification
import kotlinx.android.synthetic.main.recycle_notification_list_item.view.*

class NotificationAdapter(
    private var notificationList: List<Notification>,
    private val notificationActionClick: (String, String, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_notification_list_item, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (notificationList[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            holder.itemView.notificationRecycerview_heading.text =
                notificationList[position].notification_heading

            holder.itemView.tvNotificationDescription.text =
                notificationList[position].notification_text

            if (notificationList[position].status == "Received") {
                holder.itemView.notificationContainer.setBackgroundColor(Color.WHITE)
            }

            if (notificationList[position].status != "Received") {
                holder.itemView.notificationContainer.setOnClickListener {
                    notificationActionClick.invoke(
                        notificationList[position].id.toString(),
                        "Received",
                        position
                    )
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


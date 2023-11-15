package com.property.propertyagent.owner_panel.ui.main.home.notification.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_notification.OwnerNotificationNotification
import com.property.propertyagent.owner_panel.ui.main.home.notification.activity.NotificationOwner
import kotlinx.android.synthetic.main.recycle_notification_owner_list_item.view.*

class OwnerNotificationAdapter(
    private var requireContext : Context ,
    private var notificationList : ArrayList<OwnerNotificationNotification> ,
) : RecyclerView.Adapter<OwnerNotificationAdapter.ViewHold>() {
    private var context : Context? = null

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_notification_owner_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        if (notificationList[position].status == 1) {
            holder.itemView.notificationCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.notificationCardView.setCardBackgroundColor(Color.parseColor("#F4F4F4"))
        }
        holder.itemView.tvNotificationTitle.text = notificationList[position].title
        holder.itemView.tvNotificationContent.text = notificationList[position].description

        holder.itemView.setOnClickListener {
            holder.itemView.notificationCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            val intent = Intent(context!! , NotificationOwner::class.java)
            intent.putExtra("id" , notificationList[position].id)
            intent.putExtra("title" , notificationList[position].title)
            intent.putExtra("des" , notificationList[position].description)
            context!!.startActivity(intent)
        }
    }
}
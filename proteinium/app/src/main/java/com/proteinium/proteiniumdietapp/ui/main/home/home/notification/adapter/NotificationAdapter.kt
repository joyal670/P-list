package com.proteinium.proteiniumdietapp.ui.main.home.home.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.notifications.Notification
import com.proteinium.proteiniumdietapp.utils.getDateStringToAnotherFormat
import kotlinx.android.synthetic.main.recycerview_notification.view.*


class NotificationAdapter(private var notificationsList: List<Notification>, private val funUpdateNotification: (Int) -> Unit) : RecyclerView.Adapter<NotificationAdapter.ViewHold>()
{
    private var context: Context? = null


    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_notification, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return notificationsList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.tvNotificationTitle.text = notificationsList[position].title
        holder.itemView.tvNotificationMessage.text=notificationsList[position].message
        holder.itemView.tvNotificationDate.text=context?.getDateStringToAnotherFormat(notificationsList[position].updated_at)
        if (notificationsList[position].seen_status==0){
            holder.itemView.mcvNotificationItem.isClickable = true
            context?.let { ContextCompat.getColor(it,R.color.green_very_light)}?.let { holder.itemView.mcvNotificationItem.setCardBackgroundColor(it) }

            holder.itemView.tvNotificationTitle.setTextColor(context?.let { ContextCompat.getColor(it,R.color.green_very_dark) }!!)
            holder.itemView.tvNotificationMessage.setTextColor(context?.let { ContextCompat.getColor(it,R.color.green_very_dark) }!!)
            holder.itemView.tvNotificationDate.setTextColor(context?.let { ContextCompat.getColor(it,R.color.green_very_dark) }!!)

            holder.itemView.setOnClickListener {
                notificationsList[position].seen_status=1
                notifyDataSetChanged()
                context?.let { ContextCompat.getColor(it,R.color.light_white3)}?.let { holder.itemView.mcvNotificationItem.setCardBackgroundColor(it) }
                funUpdateNotification.invoke(notificationsList[position].id)
            }
        }

    }
}

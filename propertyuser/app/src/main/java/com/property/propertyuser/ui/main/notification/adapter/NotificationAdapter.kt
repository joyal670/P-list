package com.property.propertyuser.ui.main.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.AmenityDetail
import com.property.propertyuser.modal.user_notification.Notification
import com.property.propertyuser.pojo.ItemEventModelData
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.ui.main.home.adapter.HomeEventItemAdapter
import com.property.propertyuser.ui.main.home.events_see_all.adapter.EventsSeeAllItemAdapter
import com.property.propertyuser.utils.getDateStringToAnotherFormat
import com.property.propertyuser.utils.getTimeAgo
import com.property.propertyuser.utils.loadImagesWithGlideExt
import com.property.propertyuser.utils.loadImagesWithGlideExtNew
import kotlinx.android.synthetic.main.list_event_items.view.*
import kotlinx.android.synthetic.main.list_notification_item.view.*
import kotlinx.android.synthetic.main.list_property_detail_features_item.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class NotificationAdapter(private val context: Context,
                          private var notificationList: List<Notification>,
                          private val functionRead:(Int)->Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int):RecyclerView.ViewHolder {
        return when (viewType)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_notification_item,parent,false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS ->
            {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loader,parent,false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int
    {
        var viewtype = notificationList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvNotificationContent.text =notificationList[position].notification_heading
            holder.itemView.tvNotificationSubContent.text =notificationList[position].notification_text
            holder.itemView.tvNotificationTime.text=
                context.getTimeAgo(notificationList[position].date)
            if(notificationList[position].status.equals(context.getString(R.string.read),true)){
                holder.itemView.background=ContextCompat.getDrawable(context,R.color.white)
                holder.itemView.tvNotificationNewText.visibility=View.GONE
            }

            holder.itemView.setOnClickListener {
                if(notificationList[position].status.equals(context.getString(R.string.unread),true)){
                    functionRead.invoke(notificationList[position].id)
                    holder.itemView.background=ContextCompat.getDrawable(context,R.color.white)
                    holder.itemView.tvNotificationNewText.visibility=View.GONE
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
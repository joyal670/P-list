package com.property.propertyuser.ui.main.my_property.scheduled.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.scheduled_property.BookedTour
import com.property.propertyuser.ui.main.notification.adapter.NotificationAdapter
import com.property.propertyuser.utils.CommonMethods.isOpenRecently
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_scheduled_item.view.*


class ScheduledAdapter(private val context: Context,
                       private  var scheduledPropertyList: List<BookedTour>,
                       private val functionViewDetailsBooked: (Int) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return when (viewType)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_scheduled_item,parent,false)
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
        return scheduledPropertyList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = scheduledPropertyList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {

        if (holder is ViewHolder){
            if(scheduledPropertyList[position].property_details!=null){
                holder.itemView.tvPropertyName.text=scheduledPropertyList[position].property_details.property_name
                holder.itemView.tvPropertyCode.text =
                    context.getString(R.string.tvPropertyCode)+" "+
                            scheduledPropertyList[position].property_details.property_reg_no
            }
            if(scheduledPropertyList[position].property_priority_image!=null){
                holder.itemView.roundedBookedPropertyImage
                    .loadImagesWithGlideExt(scheduledPropertyList[position].property_priority_image.document)
            }
            holder.itemView.tvPropertyBookingDate.text=scheduledPropertyList[position].booked_date
            holder.itemView.tvPropertyBookingTime.text=scheduledPropertyList[position].time_range
            holder.itemView.tvViewDetails.setOnClickListener {
                if(isOpenRecently()) return@setOnClickListener
                functionViewDetailsBooked.invoke(scheduledPropertyList[position].property_id)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
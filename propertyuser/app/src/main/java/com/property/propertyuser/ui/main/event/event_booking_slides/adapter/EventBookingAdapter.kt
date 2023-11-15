package com.property.propertyuser.ui.main.event.event_booking_slides.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.event_package.EventPackage
import com.property.propertyuser.ui.main.event.book_event.EventBookingActivity
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_event_booking_item.view.*

class EventBookingAdapter(private val context: Context,
   private val eventPackageList:List<EventPackage>
) : RecyclerView.Adapter<EventBookingAdapter.EventBookingHolder>() {


    class EventBookingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventBookingHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_event_booking_item, parent, false)
        return EventBookingHolder(v)
    }

    override fun getItemCount(): Int {
        return eventPackageList.size
    }

    override fun onBindViewHolder(holder: EventBookingHolder, position: Int) {
        if(eventPackageList[position].event_package_image!=null){
            holder.itemView.roundedEventImage.
            loadImagesWithGlideExt(eventPackageList[position].event_package_image.package_image)
        }
        holder.itemView.tvEventAmount.text=
            context.getString(R.string.sar)+" "+eventPackageList[position].price
        if(!(eventPackageList[position].event_package_features.isNullOrEmpty())){
            holder.itemView.rvEventIncludeList.layoutManager = LinearLayoutManager(context)
            holder.itemView.rvEventIncludeList.adapter=EventsIncludedListAdapter(context,
            eventPackageList[position].event_package_features)
        }
        holder.itemView.btnBookNow.setOnClickListener {
            val intent= Intent(context,EventBookingActivity::class.java)
            intent.putExtra("package_id",eventPackageList[position].id.toString())
            context.startActivity(intent)
        }
    }
}
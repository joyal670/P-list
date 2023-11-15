package com.property.propertyuser.ui.main.event.event_booking_slides.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.event_package.EventPackageFeature
import kotlinx.android.synthetic.main.list_event_item_description.view.*

class EventsIncludedListAdapter (private val context: Context,
        private val  event_package_features: List<EventPackageFeature>)
    : RecyclerView.Adapter<EventsIncludedListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_event_item_description,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return event_package_features.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvEventDescription.text=event_package_features[position].feature_name

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
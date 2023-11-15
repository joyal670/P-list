package com.property.propertyuser.ui.main.maintenance.status.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.property.propertyuser.R
import com.property.propertyuser.utils.VectorDrawableUtils
import kotlinx.android.synthetic.main.list_status_timeline_item.view.*

class StatusTimeLineAdapter(private val context: Context)
    : RecyclerView.Adapter<StatusTimeLineAdapter.ViewHolder>() {

    private val timelineSize=4
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_status_timeline_item, parent, false)
        return ViewHolder(v,viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return timelineSize
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {

        when (position) {
            3 -> {
                holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive, ContextCompat.getColor(context,R.color.lightGray))
                holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
            }
            2 -> {
                holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive, ContextCompat.getColor(context,R.color.lightGray))
                holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
            }
            1 -> {
                holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive/*ic_marker_active*/,  ContextCompat.getColor(context,R.color.lightGray))
                holder.itemView.timeline.setEndLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
                holder.itemView.timeline.setStartLineColor(ContextCompat.getColor(context,R.color.lightGray),holder.itemViewType)
            }
            0 -> {
                holder.itemView.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive, ContextCompat.getColor(context,R.color.green_light_1))
            }
            /*else -> {
                holder.itemView.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker), ContextCompat.getColor(context,R.color.green_light_1))
            }*/
        }
    }
    inner class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.timeline.initLine(viewType)
        }
    }
}
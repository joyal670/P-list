package com.property.propertyuser.ui.main.home.events_see_all.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.events_list.Event
import com.property.propertyuser.ui.main.event.event_details.EventDetailsActivity
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_event_see_all_items.view.*

class EventsSeeAllItemAdapter(
    private val context: Context,
    private var eventsList: List<Event>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder
    {
        return when (viewtype)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_event_see_all_items,parent,false)
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
        return eventsList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = eventsList[position].id
        return when(viewtype)
        {//if data is load, returns PROGRESSBAR viewtype.
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {

        if (holder is ViewHolder){
            holder.itemView.tvEventNameSeeAll.text=eventsList[position].name

            holder.itemView.tvEventDescriptionSeeAll.text=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(eventsList[position].short_description, Html.FROM_HTML_MODE_COMPACT)
            }
            else{
                Html.fromHtml(eventsList[position].short_description)
            }
            if(eventsList[position].event_priority_image!=null){
                holder.itemView.ivEventImageSeeAll.
                loadImagesWithGlideExt(eventsList[position].event_priority_image.image)
            }else{
                holder.itemView.ivEventImageSeeAll.
                loadImagesWithGlideExt("")
            }
            holder.itemView.mcvEventSeeAll.setOnClickListener {
                val intent=Intent(context,
                    EventDetailsActivity::class.java)
                intent.putExtra("event_id",eventsList[position].id)
                context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }

}
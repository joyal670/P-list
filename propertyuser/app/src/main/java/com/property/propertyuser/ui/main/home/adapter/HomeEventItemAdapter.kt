package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.ui.main.event.event_details.EventDetailsActivity
import kotlinx.android.synthetic.main.list_event_items.view.*

class HomeEventItemAdapter (private val context: Context,private val eventsList:List<Event>)
    : RecyclerView.Adapter<HomeEventItemAdapter.ViewHolder>(){

    /*private  val eventsList= listOf<String>("Event Name",
        "Event Name",
        "Event Name",
        "Event Name")*/
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_event_items,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        if(eventsList[position].event_priority_image!=null){
            Glide.with(context).load(eventsList[position].event_priority_image.image).into(holder.itemView.ivEventImage)
        }
        holder.itemView.tvEventName.text=eventsList[position].name
        holder.itemView.tvEventDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(eventsList[position].short_description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(eventsList[position].short_description)
        }
        //holder.itemView.tvEventDescription.text=eventsList[position].short_description
        holder.itemView.cardEvent.setOnClickListener {
            val intent=Intent(context,
                EventDetailsActivity::class.java)
            intent.putExtra("event_id",eventsList[position].id)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.events

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleParentEventListItemBinding
import com.iroid.jeetmeet.modal.parent.events.new.ParentEventsNewData


class ParentEventAdapter(private var eventsList: ArrayList<ParentEventsNewData>) :
    RecyclerView.Adapter<ParentEventAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleParentEventListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleParentEventListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvEventName.text = eventsList[position].title
        if (eventsList[position].type == 0) {
            holder.binding.tvEventType.text = ": Common For All"
            holder.binding.tvEventClass.text = ": Common For All"
        } else {
            holder.binding.tvEventType.text = ": Assigned"
            holder.binding.tvEventClass.text =
                ": " + eventsList[position].`class`.toString() + " " + eventsList[position].divisionName.toString()
        }
        holder.binding.tvEventDate.text = ": " + eventsList[position].date
        holder.binding.tvEventExDate.text = ": " + eventsList[position].ex_date
        holder.binding.tvEventTime.text = ": " + eventsList[position].time


    }
}

package com.property.propertyuser.ui.main.side_menu.requested_property.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.desired_property_request_list.Requested
import com.property.propertyuser.ui.main.notification.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.list_requested_property_ticket_item.view.*

class RequestedPropertyTicketAdapter(private val context: Context,
                                     private var requestedList:List<Requested>,
                                     private val function: (Int) -> Unit)
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
                    .inflate(R.layout.list_requested_property_ticket_item,parent,false)
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
        return requestedList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = requestedList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvRequestId.text =context.getString(R.string.tvRequestId)+" "+
                    requestedList[position].request_id_no
            holder.itemView.btnViewDetails.setOnClickListener {
                function.invoke(requestedList[position].id)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
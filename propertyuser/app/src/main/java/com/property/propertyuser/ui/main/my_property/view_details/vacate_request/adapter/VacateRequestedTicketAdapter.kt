package com.property.propertyuser.ui.main.my_property.view_details.vacate_request.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.vacate_request_list.UserVacateRequest
import kotlinx.android.synthetic.main.list_vacate_requested_ticket_item.view.*

class VacateRequestedTicketAdapter(private val context: Context,
                                   private var userVacateRequests: List<UserVacateRequest>,
                                   private  val function: (Int) -> Unit)
    : RecyclerView.Adapter<VacateRequestedTicketAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_vacate_requested_ticket_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userVacateRequests.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvRequestId.text =context.getString(R.string.tvRequestId)+userVacateRequests[position].vacate_request_id.toString()
        holder.itemView.tvRequestDate.text = context.getString(R.string.tvRequestDate) + " " + userVacateRequests[position].vacating_date
        holder.itemView.btnViewDetails.setOnClickListener {
            function.invoke(userVacateRequests[position].vacate_request_id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
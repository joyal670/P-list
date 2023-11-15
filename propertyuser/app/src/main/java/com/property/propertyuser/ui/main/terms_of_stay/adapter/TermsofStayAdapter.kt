package com.property.propertyuser.ui.main.terms_of_stay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import kotlinx.android.synthetic.main.list_terms_of_stay_item.view.*


class TermsofStayAdapter(private val context: Context)
    : RecyclerView.Adapter<TermsofStayAdapter.ViewHolder>() {

    private  val bookingList= listOf<String>("Booking",
    "Booking")

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_terms_of_stay_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvBookingText.text =bookingList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
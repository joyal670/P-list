package com.property.propertyuser.ui.main.property_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.AmenityCategory
import kotlinx.android.synthetic.main.list_property_main_amenities.view.*

class PropertyAmenitiesAdapter(
    private val context: Context,
    private var amenityDetails: ArrayList<AmenityCategory>
) : RecyclerView.Adapter<PropertyAmenitiesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_main_amenities, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return amenityDetails.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.itemView.tvPropertyDetailsFeatured.text = amenityDetails[position].name
        holder.itemView.rvAmenity.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        holder.itemView.rvAmenity.adapter =
            PropertySubAmenitiesAdapter(context, amenityDetails[position].amenity_details)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
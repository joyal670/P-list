package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityCategory
import kotlinx.android.synthetic.main.list_property_amenities.view.*

class PropertyViewAmenitiesMainAdapter(
    private var amenityCategoryList: List<AmenityCategory>,
) : RecyclerView.Adapter<PropertyViewAmenitiesMainAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_amenities, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return amenityCategoryList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.itemView.tvAmenityCategory.text = amenityCategoryList[position].name

        holder.itemView.rvSubAmenities.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val amenityAdapter =
            PropertyViewAmenitiesSubAdapter(amenityCategoryList[position].amenity_details)
        holder.itemView.rvSubAmenities.adapter = amenityAdapter
    }
}
package com.property.propertyuser.ui.main.property_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.AmenityDetail
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_property_detail_features_item.view.*


class PropertySubAmenitiesAdapter(
    private val context: Context,
    private var amenityDetails: List<AmenityDetail>
) : RecyclerView.Adapter<PropertySubAmenitiesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_detail_features_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return amenityDetails.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.itemView.ivCardPropertyItemFeatures.loadImagesWithGlideExt(amenityDetails[position].image)
        holder.itemView.tvPropertyFeaturedItemName.text = amenityDetails[position].name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
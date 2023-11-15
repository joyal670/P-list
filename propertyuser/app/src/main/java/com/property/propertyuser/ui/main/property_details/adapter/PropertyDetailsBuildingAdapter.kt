package com.property.propertyuser.ui.main.property_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.AmenityDetail
import com.property.propertyuser.pojo.ItemEventModelData
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.ui.main.home.adapter.HomeEventItemAdapter
import com.property.propertyuser.utils.loadImagesWithGlideExt
import com.property.propertyuser.utils.loadImagesWithGlideExtNew
import kotlinx.android.synthetic.main.list_event_items.view.*
import kotlinx.android.synthetic.main.list_property_detail_features_item.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class PropertyDetailsBuildingAdapter(private val context: Context,
                    private var amenityDetails: List<AmenityDetail>)
    : RecyclerView.Adapter<PropertyDetailsBuildingAdapter.ViewHolder>() {

    private  val propertyDetailsBuildingsIconsList= listOf<Int>(R.drawable.ic_home_items,
        R.drawable.ic_home_items,R.drawable.ic_home_items)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_detail_features_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return amenityDetails.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvPropertyFeaturedItemName.text =amenityDetails[position].name
        holder.itemView.ivCardPropertyItemFeatures.loadImagesWithGlideExtNew(amenityDetails[position].image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
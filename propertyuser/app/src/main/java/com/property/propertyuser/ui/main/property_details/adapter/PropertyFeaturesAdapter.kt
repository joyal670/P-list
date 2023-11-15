package com.property.propertyuser.ui.main.property_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.AmenitiesModelData
import com.property.propertyuser.ui.main.filter.adapter.AmenitiesAdapter
import com.property.propertyuser.ui.main.filter.adapter.PropertyTypeAdapter
import com.property.propertyuser.utils.leftDrawable
import kotlinx.android.synthetic.main.list_property_features_item.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class PropertyFeaturesAdapter (private val context: Context)
    : RecyclerView.Adapter<PropertyFeaturesAdapter.ViewHolder>(){

    private  val propertyFeaturesList= listOf<String>("04",
        "03",
        "115 sq. M.",
        "Fully",
        "02")
    private  val propertyFeaturesIconsList= listOf<Int>(R.drawable.ic_bed,
    R.drawable.ic_bath,R.drawable.ic_diameter,R.drawable.ic_sofa,R.drawable.ic_block)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_features_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return propertyFeaturesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvPropertyDetailsFeatured.text =propertyFeaturesList[position]
        holder.itemView.ivIconFetured.setImageResource(propertyFeaturesIconsList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
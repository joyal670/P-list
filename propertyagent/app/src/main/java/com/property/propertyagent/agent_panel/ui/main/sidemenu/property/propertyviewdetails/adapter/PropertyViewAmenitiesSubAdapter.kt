package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityDetail
import com.property.propertyagent.utils.loadImagesWithGlideExtByFitCenter
import kotlinx.android.synthetic.main.list_property_detail_features_item.view.*

class PropertyViewAmenitiesSubAdapter(
    private var amenityDetailList: List<AmenityDetail>,
) : RecyclerView.Adapter<PropertyViewAmenitiesSubAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_detail_features_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return amenityDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.itemView.tvPropertyFeaturedItemName.text = amenityDetailList[position].name
        if (!amenityDetailList[position].name.equals(null)) {
            holder.itemView.ivCardPropertyItemFeatures.loadImagesWithGlideExtByFitCenter(
                amenityDetailList[position].image
            )
        }
    }
}
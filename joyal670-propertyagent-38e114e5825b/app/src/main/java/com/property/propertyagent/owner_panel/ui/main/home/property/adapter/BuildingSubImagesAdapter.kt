package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingBuildingApartment
import com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details.PropertyMainDetailedActivity
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import kotlinx.android.synthetic.main.recyerview_owner_property_images_list_item.view.*

class BuildingSubImagesAdapter(private var buildingApartments : List<OwnerPropertyMainListingBuildingApartment>) :
    RecyclerView.Adapter<BuildingSubImagesAdapter.ViewHold>() {
    private lateinit var context : Context

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyerview_owner_property_images_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return if (buildingApartments.size <= 6) {
            buildingApartments.size
        } else {
            6
        }
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {

        holder.itemView.tvBuildingType.text = buildingApartments[position].type_details.type
        Glide.with(context).load(buildingApartments[position].property_priority_image.document)
            .into(holder.itemView.ivBuildingImage)

        val status = buildingApartments[position].occupied
        if (status == 0) {
            holder.itemView.tvBuildingStatus.setTextColor(Color.parseColor("#EABC6B"))
            holder.itemView.tvBuildingStatus.text = context.getString(R.string.vacant)
        } else {
            holder.itemView.tvBuildingStatus.setTextColor(Color.parseColor("#6AC58C"))
            holder.itemView.tvBuildingStatus.text = context.getString(R.string.occupied)
        }

        holder.itemView.recyerview_adapter_building_layout.setOnClickListener {
            clicked_property_id = buildingApartments[position].id.toString()
            context.startActivity(Intent(context , PropertyMainDetailedActivity::class.java))
        }
    }
}
package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListBuildingApartment
import com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details.PropertyMainDetailedActivity
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import kotlinx.android.synthetic.main.recyerview_owner_detailed_property_images_list_item.view.*


class OwnerDetailedImagesAdapter(
    context : Context ,
    private val buildingList : ArrayList<OwnerBuildingDetailsListBuildingApartment> ,
) : RecyclerView.Adapter<OwnerDetailedImagesAdapter.ViewHold>() {
    private lateinit var context : Context

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyerview_owner_detailed_property_images_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return buildingList.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        /* for divider line */
        if ((holder.absoluteAdapterPosition) % 2 == 1) {
            holder.itemView.recyerview_details_adapter_dividerView.visibility = View.INVISIBLE
        }

        Glide.with(context).load(buildingList[position].property_priority_image.document)
            .into(holder.itemView.recyerview_details_adapter_building_image)
        holder.itemView.recyerview_details_adapter_building_type.text =
            buildingList[position].type_details.type
        holder.itemView.tvPropertyName.text = buildingList[position].property_name
        holder.itemView.tvPropertyCode.text = buildingList[position].property_reg_no

        if (buildingList[position].occupied == 0) {
            holder.itemView.recyerview_details_adapter_building_status.text =
                context.getString(R.string.vacant)
            holder.itemView.recyerview_details_adapter_building_status.setTextColor(Color.parseColor(
                "#EABC6B"))
            holder.itemView.recyclerview_occupied1.visibility = View.GONE
            holder.itemView.recyclerview_occupied2.visibility = View.GONE

            holder.itemView.tvVacant.text = buildingList[position].vacated_since
            holder.itemView.tvRentTime.text = buildingList[position].rent_out_count.toString()
        } else {
            holder.itemView.recyerview_details_adapter_building_status.text =
                context.getString(R.string.occupied)
            holder.itemView.recyerview_details_adapter_building_status.setTextColor(Color.parseColor(
                "#6AC58C"))
            holder.itemView.recyclerview_vacant1.visibility = View.GONE
            holder.itemView.recyclerview_vacant2.visibility = View.GONE

            holder.itemView.tvOccupiedFrom.text = buildingList[position].occupied_from
            holder.itemView.tvOccupiedTo.text = buildingList[position].occupied_to
        }

        holder.itemView.recyerview_details_adapter_building_layout.setOnClickListener {
            clicked_property_id = buildingList[position].id.toString()
            context.startActivity(Intent(context , PropertyMainDetailedActivity::class.java))
        }

    }
}
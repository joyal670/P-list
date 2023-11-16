package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityDetail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subCatagory
import kotlinx.android.synthetic.main.recycer_amenities.view.*

class AgentAmenitiesSubAdapter(
    private var amenities: List<Amenity_subCatagory>,
    private var passedAmenities: List<AmenityDetail>,
    private var addItem: (Int) -> Unit,
    private var remove: (Int) -> Unit,
) : RecyclerView.Adapter<AgentAmenitiesSubAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_amenities, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return amenities.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        context?.let {
            Glide.with(it).load(amenities[position].image)
                .into(holder.itemView.ivAmenities)
        }

        holder.itemView.typeAmenities.text = amenities[position].name

        holder.itemView.ChkAmenities.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i("TAG", buttonView.toString())
            if (isChecked) {
                addItem.invoke(amenities[position].id)
            } else {
                remove.invoke(amenities[position].id)
            }
        }
        if (!passedAmenities.equals(null)) {
            Log.i("TAG", "passed amenities: $passedAmenities")
            for (i in passedAmenities.indices) {
                Log.i("TAG", "amenities:" + amenities[position].id.toString())
                if (passedAmenities[i].id == amenities[position].id) {
                    Log.e("check pos", position.toString())
                    Log.e("check details", amenities[position].name)
                    holder.itemView.ChkAmenities.isChecked = true
                    addItem.invoke(amenities[position].id)
                }
            }
        }
    }
}

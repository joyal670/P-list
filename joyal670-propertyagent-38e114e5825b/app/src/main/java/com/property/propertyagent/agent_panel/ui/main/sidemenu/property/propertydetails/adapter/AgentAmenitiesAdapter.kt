package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityCategory
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import kotlinx.android.synthetic.main.recycer_main_amenities.view.*

class AgentAmenitiesAdapter(
    private var amenitiesList: ArrayList<Amenity_subTitle>,
    private var passedAmenitiesList: ArrayList<AmenityCategory>,
    private var addItem: (Int) -> Unit,
    private var removeItem: (Int) -> Unit,
) : RecyclerView.Adapter<AgentAmenitiesAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_main_amenities, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return amenitiesList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.tv_mainAmineity.text = amenitiesList[position].name
        holder.itemView.rvSubAmenities.layoutManager = LinearLayoutManager(context)
        try {
            if (passedAmenitiesList.size != 0) {
                Log.i(
                    "TAG234",
                    "onBindViewHolder:passed list" + passedAmenitiesList[position].amenity_details
                )
                holder.itemView.rvSubAmenities.adapter =
                    AgentAmenitiesSubAdapter(amenitiesList[position].amenities,
                        passedAmenitiesList[position].amenity_details,
                        { addItem(it) }) { removeItem(it) }
            } else {
                holder.itemView.rvSubAmenities.adapter =
                    AgentAmenitiesSubAdapter(amenitiesList[position].amenities,
                        passedAmenitiesList[-1].amenity_details,
                        { addItem(it) }) { removeItem(it) }
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    private fun removeItem(pos: Int) {
        removeItem.invoke(pos)
    }

    private fun addItem(pos: Int) {
        addItem.invoke(pos)
    }
}


package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.AvailabilityModelData
import com.property.propertyuser.pojo.BedRoomsModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class AvailabilityAdapter  (private val context: Context, private val availabilityList : List<AvailabilityModelData>)
    : RecyclerView.Adapter<AvailabilityAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return availabilityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val availabilityItem = availabilityList[position]
        holder.itemView.checkbox.text=availabilityItem.itemName
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
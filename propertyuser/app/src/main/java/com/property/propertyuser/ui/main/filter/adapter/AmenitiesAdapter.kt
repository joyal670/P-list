package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.main_filter_details.Amenity
import com.property.propertyuser.pojo.AmenitiesModelData
import com.property.propertyuser.pojo.BedRoomsModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class AmenitiesAdapter  (private val context: Context,
                         private val amenitiesList : List<Amenity>,
                         private val funSetAmenities: (Int) -> Unit,
                         private val funRemoveAmenities: (Int) -> Unit)
    : RecyclerView.Adapter<AmenitiesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return amenitiesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val amenitiesItem = amenitiesList[position]
        holder.itemView.checkbox.text=amenitiesItem.name
        holder.itemView.checkbox.isChecked=amenitiesItem.checked
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                amenitiesItem.checked=true
                funSetAmenities.invoke(amenitiesItem.id)
            } else {
                amenitiesItem.checked=false
                funRemoveAmenities.invoke(amenitiesItem.id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
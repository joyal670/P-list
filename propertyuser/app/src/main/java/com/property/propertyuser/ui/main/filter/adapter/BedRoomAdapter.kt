package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.BedRoomsModelData
import com.property.propertyuser.pojo.RentalFrequencyModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class BedRoomAdapter  (private val context: Context,
                       private val bedRoomList : List<BedRoomsModelData>,
                       private val funSetBed: (Int) -> Unit,
                       private val funRemoveBed: (Int) -> Unit)
    : RecyclerView.Adapter<BedRoomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return bedRoomList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val bedRoomItem = bedRoomList[position]
        holder.itemView.checkbox.text=bedRoomItem.itemName
        holder.itemView.checkbox.isChecked=bedRoomItem.checked
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bedRoomItem.checked=true
                funSetBed.invoke(bedRoomItem.id)
            } else {
                bedRoomItem.checked=false
                funRemoveBed.invoke(bedRoomItem.id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
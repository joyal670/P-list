package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.BedRoomsModelData
import com.property.propertyuser.pojo.FurnishingModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class FurnishingAdapter  (private val context: Context,
                          private val furnishingList : List<FurnishingModelData>,
                          private val funSetFurnished: (Int) -> Unit,
                          private val funRemoveFurnished: (Int) -> Unit)
    : RecyclerView.Adapter<FurnishingAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return furnishingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val furnishingItem = furnishingList[position]
        holder.itemView.checkbox.text=furnishingItem.itemName
        holder.itemView.checkbox.isChecked=furnishingItem.checked
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                furnishingItem.checked=true
                funSetFurnished.invoke(furnishingItem.id)
            } else {
                furnishingItem.checked=false
                funRemoveFurnished.invoke(furnishingItem.id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
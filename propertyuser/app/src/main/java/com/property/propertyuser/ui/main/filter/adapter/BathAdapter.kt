package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.BathsModelData
import com.property.propertyuser.pojo.BedRoomsModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class BathAdapter (private val context: Context,
                   private val bathList : List<BathsModelData>,
                   private val funSetBath: (Int) -> Unit,
                   private val funRemoveBath: (Int) -> Unit)
    : RecyclerView.Adapter<BathAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return bathList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val bathItem = bathList[position]
        holder.itemView.checkbox.text=bathItem.itemName
        holder.itemView.checkbox.isChecked=bathItem.checked
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bathItem.checked=true
                funSetBath.invoke(bathItem.id)
            } else {
                bathItem.checked=false
                funRemoveBath.invoke(bathItem.id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
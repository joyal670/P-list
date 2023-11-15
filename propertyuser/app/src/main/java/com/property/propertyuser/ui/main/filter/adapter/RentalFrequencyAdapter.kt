package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.main_filter_details.Frequency
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.pojo.RentalFrequencyModelData
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class RentalFrequencyAdapter (private val context: Context,
                              private val rentalFrequencyList : List<Frequency>,
                              private val funSetFrequency: (Int) -> Unit,
                              private val funRemoveFrequency: (Int) -> Unit)
    : RecyclerView.Adapter<RentalFrequencyAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items,parent,false)
        return ViewHolder(
                v
        )
    }

    override fun getItemCount(): Int {
        return rentalFrequencyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val rentalItem = rentalFrequencyList[position]
        holder.itemView.checkbox.text=rentalItem.type
        holder.itemView.checkbox.isChecked=rentalItem.checked
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                rentalItem.checked=true
                funSetFrequency.invoke(rentalItem.id)
            }
            else{
                rentalItem.checked=false
                funRemoveFrequency.invoke(rentalItem.id)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
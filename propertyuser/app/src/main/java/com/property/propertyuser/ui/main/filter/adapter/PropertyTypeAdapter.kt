package com.property.propertyuser.ui.main.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.databinding.FragmentSimilarProductsItemBinding
import com.property.propertyuser.databinding.ListPropertyTypeItemsBinding
import com.property.propertyuser.modal.main_filter_details.Category
import com.property.propertyuser.pojo.ItemEventModelData
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.ui.main.home.adapter.HomeEventItemAdapter
import kotlinx.android.synthetic.main.list_event_items.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class PropertyTypeAdapter(private val context: Context,
                          private var propertyTypeList: List<Category>,
                          private val funSetPropertyType: (Int) -> Unit,
                          private val funRemovePropertyType: (Int) -> Unit)
    : RecyclerView.Adapter<PropertyTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val view = ListPropertyTypeItemsBinding.inflate(LayoutInflater.from(context) , parent,false)
        return ViewHolder(view)
        /*val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_property_type_items, parent, false)
        return ViewHolder(v)*/
    }

    override fun getItemCount(): Int {
        return propertyTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.binding.checkbox.text =propertyTypeList[position].type
        holder.binding.checkbox.isChecked=propertyTypeList[position].checked
        holder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Log.e("checked",isChecked.toString())
                propertyTypeList[position].checked=true
                funSetPropertyType.invoke(propertyTypeList[position].id)
            }else{
                Log.e("checked",isChecked.toString())
                propertyTypeList[position].checked=false
                funRemovePropertyType.invoke(propertyTypeList[position].id)
            }
        }

    }

    class ViewHolder(var binding: ListPropertyTypeItemsBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
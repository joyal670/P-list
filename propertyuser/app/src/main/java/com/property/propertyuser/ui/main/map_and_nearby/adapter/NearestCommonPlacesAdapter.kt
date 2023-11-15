package com.property.propertyuser.ui.main.map_and_nearby.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.pojo.ItemEventModelData
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.ui.main.home.adapter.HomeEventItemAdapter
import kotlinx.android.synthetic.main.list_common_places_item.view.*
import kotlinx.android.synthetic.main.list_event_items.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*

class NearestCommonPlacesAdapter(private val context: Context,
            private val funSelectedNearByPlace:(String)->Unit)
    : RecyclerView.Adapter<NearestCommonPlacesAdapter.ViewHolder>() {

    private  val commonPlacesList= listOf<String>(
        context?.getString(R.string.school),
        context?.getString(R.string.mosque),
        context?.getString(R.string.hotel),
        context?.getString(R.string.malls),
        context?.getString(R.string.hospital),
        context?.getString(R.string.bank)
    )
    private var rowIndex:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_common_places_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return commonPlacesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.btnCommonPlaceItem.text =commonPlacesList[position]
        holder.itemView.btnCommonPlaceItem.setOnClickListener {
            rowIndex=position
            notifyDataSetChanged()
            funSelectedNearByPlace.invoke(commonPlacesList[position])
        }
        if(rowIndex==position){
            holder.itemView.btnCommonPlaceItem.backgroundTintList=ContextCompat.getColorStateList(
                context,R.color.green_solid)
            holder.itemView.btnCommonPlaceItem.setTextColor(ContextCompat.getColor(
                context,R.color.white))
        }
        else{
            holder.itemView.btnCommonPlaceItem.backgroundTintList=ContextCompat.getColorStateList(
                context,R.color.white)
            holder.itemView.btnCommonPlaceItem.setTextColor(ContextCompat.getColor(
                context,R.color.black))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyuser.BuildConfig
import com.property.propertyuser.R
import kotlinx.android.synthetic.main.list_home_property_item.view.*
import kotlinx.android.synthetic.main.list_selected_filter_item.view.*
import java.io.File


class HomeSeletedFilteredItemAdapter  (private var filteredList:List<String>,
    private val context: Context,private val  functionFilterSelected:(Int)->Unit)
    : RecyclerView.Adapter<HomeSeletedFilteredItemAdapter.ViewHolder>(){

    private var rowIndex:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_selected_filter_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvFilterItem.text=filteredList[position]
        holder.itemView.tvFilterItem.setOnClickListener {
            rowIndex=position
            notifyDataSetChanged()
            functionFilterSelected.invoke(position)
        }
        if(rowIndex==position){
            holder.itemView.tvFilterItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_white_radius_12_inner_white)
            holder.itemView.tvFilterItem.setTextColor(ContextCompat.getColor(context,R.color.green_solid))
        }
        else
        {
            holder.itemView.tvFilterItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_white_radius_12)
            holder.itemView.tvFilterItem.setTextColor(ContextCompat.getColor(context,R.color.white))
        }

    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.filter_home.BuyModel
import kotlinx.android.synthetic.main.list_home_filter_sub_item.view.*


class HomeSeletedFilteredMainSubCategoryItemAdapter_0  (private val context: Context,
                                                        private var  selectedFilterMainSubItemsBuy: List<BuyModel>,
                                                        private val  functionFilterSubMain:(Int)->Unit)
    : RecyclerView.Adapter<HomeSeletedFilteredMainSubCategoryItemAdapter_0.ViewHolder>(){


    private var rowIndex:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home_filter_sub_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return selectedFilterMainSubItemsBuy.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvFilterSubItem.text=selectedFilterMainSubItemsBuy[position].buyModelName
        if(selectedFilterMainSubItemsBuy[position].checked){
            holder.itemView.tvFilterSubItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_white_greenlight)
            holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.green_solid))
            holder.itemView.tvFilterSubItem.
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle,0,0,0)
        }
        else{
            holder.itemView.tvFilterSubItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_greenlight_green)
            holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.itemView.tvFilterSubItem.
            setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }
        holder.itemView.tvFilterSubItem.setOnClickListener {
            rowIndex=position
            notifyDataSetChanged()
            functionFilterSubMain.invoke(position)
        }
        if(rowIndex==position){
            selectedFilterMainSubItemsBuy[position].checked=true
            holder.itemView.tvFilterSubItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_white_greenlight)
            holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.green_solid))
            holder.itemView.tvFilterSubItem.
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle,0,0,0)
        }
        else
        {
            selectedFilterMainSubItemsBuy[position].checked=false
            holder.itemView.tvFilterSubItem.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_greenlight_green)
            holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.itemView.tvFilterSubItem.
            setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
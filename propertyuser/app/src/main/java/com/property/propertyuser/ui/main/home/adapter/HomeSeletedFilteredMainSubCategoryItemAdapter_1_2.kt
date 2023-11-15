package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.filter_home.Category
import com.property.propertyuser.modal.filter_home.CommercialModel
import kotlinx.android.synthetic.main.list_home_filter_sub_item.view.*


class HomeSeletedFilteredMainSubCategoryItemAdapter_1_2  (private val context: Context,
                                                          private var selectedFilterMainSubItemsComm:List<Category>,
                                                          /*private var selectedFilterMainSubItemsComm:List<CommercialModel>,*/
                                                          private val  functionFilterSubMain:(Int)->Unit)
    : RecyclerView.Adapter<HomeSeletedFilteredMainSubCategoryItemAdapter_1_2.ViewHolder>(){


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
        return selectedFilterMainSubItemsComm.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvFilterSubItem.text=selectedFilterMainSubItemsComm[position].type
        if(selectedFilterMainSubItemsComm[position].checked){
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
            if(selectedFilterMainSubItemsComm[position].checked){
                selectedFilterMainSubItemsComm[position].checked=false
                holder.itemView.tvFilterSubItem.background=ContextCompat.
                getDrawable(context,R.drawable.bg_round_border_greenlight_green)
                holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.white))
                holder.itemView.tvFilterSubItem.
                setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            }
            else
            {
                selectedFilterMainSubItemsComm[position].checked=true
                holder.itemView.tvFilterSubItem.background=ContextCompat.
                getDrawable(context,R.drawable.bg_round_border_white_greenlight)
                holder.itemView.tvFilterSubItem.setTextColor(ContextCompat.getColor(context,R.color.green_solid))
                holder.itemView.tvFilterSubItem.
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle,0,0,0)
            }
            functionFilterSubMain.invoke(position)
        }

    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
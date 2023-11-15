package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.filter_home.RentModel
import kotlinx.android.synthetic.main.list_home_filter_main_item.view.*


class HomeSeletedFilteredMainCategoryItemAdapter_0  (private val context: Context,
                                                     private var  selectedFilterMainItems: List<RentModel>,
                                                     private val  functionFilterMainMain:(String)->Unit)
    : RecyclerView.Adapter<HomeSeletedFilteredMainCategoryItemAdapter_0.ViewHolder>(){

    private var rowIndex:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home_filter_main_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return selectedFilterMainItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvFilterItemMain.text=selectedFilterMainItems[position].rentModelName
        holder.itemView.tvFilterItemMain.setOnClickListener {
            rowIndex=position
            notifyDataSetChanged()
            functionFilterMainMain.invoke(selectedFilterMainItems[position].rentModelName)
        }
        if(rowIndex==position){
            selectedFilterMainItems[position].checked=true
            holder.itemView.tvFilterItemMain.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_white_greenlight)
            holder.itemView.tvFilterItemMain.setTextColor(ContextCompat.getColor(context,R.color.green_solid))
            holder.itemView.tvFilterItemMain.
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle,0,0,0)
        }
        else
        {
            selectedFilterMainItems[position].checked=false
            holder.itemView.tvFilterItemMain.background=ContextCompat.
            getDrawable(context,R.drawable.bg_round_border_greenlight_green)
            holder.itemView.tvFilterItemMain.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.itemView.tvFilterItemMain.
            setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
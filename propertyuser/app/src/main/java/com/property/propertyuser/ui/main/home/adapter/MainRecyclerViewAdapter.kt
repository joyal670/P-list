package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property
import com.property.propertyuser.ui.main.home.events_see_all.EventsSeeAllActivity
import com.property.propertyuser.utils.CommonMethods
import kotlinx.android.synthetic.main.event_recycler_view.view.*
import kotlinx.android.synthetic.main.list_home_property_item.view.*

class MainRecyclerViewAdapter(private val context: Context, private val propertyList:List<Property>,
                              private val eventsList:List<Event>, private val functionInfo: (Int,String) -> Unit,
                              private val functionMap:(Int)->Unit, private val functionBook:(Int)->Unit
                              , private val functionAddToFav:(Int)->Unit, private val functionRemoveFromFav:(Int)->Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }



    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(property: Property) {
            if(property.documents.isNotEmpty()){
                CommonMethods.setImage(context,property.documents[0].document,itemView.ivPropertyImage)
            }
            itemView.tvPropertyAmount.text="SAR "+property.selling_price
            itemView.tvPropertyDiscountAmount.text="SAR "+property.mrp
            itemView.tvRating.text=property.rating
            itemView.tvBedCount.text=property.Beds.toString()
            itemView.tvBathCount.text=property.Bathroom.toString()
            itemView.tvDiameter.text=property.area.toString() +"sq. M."
            //itemView.iconFavourite.isCheckable = propertyData.is_favourite==1
            if(property.is_favourite==1){
                itemView.iconFavourite.visibility=View.GONE
                itemView.iconFavouriteSelected.visibility=View.VISIBLE
            }else{
                itemView.iconFavouriteSelected.visibility=View.GONE
                itemView.iconFavourite.visibility=View.VISIBLE
            }
            if(property.is_featured==1){
                itemView.ivGreenTag.visibility=View.VISIBLE
            }else{
                itemView.ivGreenTag.visibility=View.GONE
            }
            itemView.iconFavourite.setOnClickListener {
                itemView.iconFavourite.visibility=View.GONE
                itemView.iconFavouriteSelected.visibility=View.VISIBLE
                property.is_favourite=1
                functionAddToFav.invoke(property.id)
            }
            itemView.iconFavouriteSelected.setOnClickListener {
                itemView.iconFavouriteSelected.visibility=View.GONE
                itemView.iconFavourite.visibility=View.VISIBLE
                property.is_favourite=0
                functionRemoveFromFav.invoke(property.id)
            }
        }
    }
    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.tvEventsSeeAll.setOnClickListener {
                context.startActivity(Intent(context,EventsSeeAllActivity::class.java))
            }
            itemView.rvEventsList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            itemView.rvEventsList.adapter=HomeEventItemAdapter(context,eventsList)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_home_property_item, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_recycler_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(propertyList[position])
        holder.itemView.ivInfo.setOnClickListener {
            functionInfo.invoke(propertyList[position].id,"")
        }
        holder.itemView.ivCalender.setOnClickListener {
            functionBook.invoke(propertyList[position].id)
        }
//        if (propertyList[position].viewType === VIEW_TYPE_ONE) {
//            (holder as View1ViewHolder).bind(propertyList[position])
//        } else {
//            (holder as View2ViewHolder).bind(position)
//        }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
}
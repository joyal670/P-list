package com.property.propertyuser.ui.main.side_menu.requested_property.generated_ticket_requested_property.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.modal.desired_property_request_details.DesiredProperty
import com.property.propertyuser.modal.desired_property_request_details.PropertyPriorityImage
import com.property.propertyuser.utils.CommonMethods
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.list_requested_property_generated_ticket_item.view.*

class GeneratedTicketRequestedPropertyDetailsAdapter(private val context: Context,
                                                     private var propertyList: List<DesiredProperty>,
                                                     private val function: (Int) -> Unit,
                                                     private val functionAddToFav:(Int,Int)->Unit,
                                                     private val functionRemoveFromFav:(Int,Int)->Unit)
    : RecyclerView.Adapter<GeneratedTicketRequestedPropertyDetailsAdapter.ViewHolder>() {

    private var imagePropertyList= ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_requested_property_generated_ticket_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        if(propertyList[position].property_to==0){
                holder.itemView.tvAppartmentText.text=context.getString(R.string.tvAppartmentText)
                holder.itemView.tvPropertyAmount.text= context.getText(R.string.sar).toString() +" "+propertyList[position].rent
                holder.itemView.tvPropertyDiscountAmount.visibility=View.GONE
            }else{
                holder.itemView.tvAppartmentText.text=context.getString(R.string.tvAppartmentSaleText)
                holder.itemView.tvPropertyAmount.text= context.getText(R.string.sar).toString() +" "+propertyList[position].selling_price
                holder.itemView.tvPropertyDiscountAmount.text= context.getText(R.string.sar).toString()+" "+propertyList[position].mrp
            }
            holder.itemView.tvPropertyDiscountAmount.paintFlags =
                holder.itemView.tvPropertyDiscountAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.tvRating.text=propertyList[position].rating
            holder.itemView.tvBedCount.text= propertyList[position].Beds.toString()
            holder.itemView.tvBathCount.text=propertyList[position].Bathroom.toString()
            holder.itemView.tvDiameter.text=propertyList[position].area.toString()+context?.getString(R.string.sq_m)

            if(propertyList[position].property_priority_image!=null){
                Glide.with(context)
                    .load(propertyList[position].property_priority_image.document).into(holder.itemView.ivPropertyImage)
            }
            else{
                Glide.with(context).load(R.drawable.ic_image_placeholder_new).into(holder.itemView.ivPropertyImage)
            }
            Log.e("check",propertyList[position].is_favourite.toString())
            if(propertyList[position].is_favourite==1){
                holder.itemView.iconFavourite.visibility=View.GONE
                holder.itemView.iconFavouriteSelected.visibility=View.VISIBLE
                Log.e("check11",propertyList[position].is_favourite.toString())
            }else{
                holder.itemView.iconFavouriteSelected.visibility=View.GONE
                holder.itemView.iconFavourite.visibility=View.VISIBLE
                Log.e("check22",propertyList[position].is_favourite.toString())
            }
            if(propertyList[position].is_featured==1){
                holder.itemView.ivGreenTag.visibility=View.VISIBLE
            }else{
                holder.itemView.ivGreenTag.visibility=View.GONE
            }
            holder.itemView.iconFavourite.setOnClickListener {
                holder.itemView.iconFavourite.visibility=View.GONE
                holder.itemView.iconFavouriteSelected.visibility=View.VISIBLE
                propertyList[position].is_favourite=1
                functionAddToFav.invoke(propertyList[position].id,position)
            }
            holder.itemView.iconFavouriteSelected.setOnClickListener {
                holder.itemView.iconFavouriteSelected.visibility=View.GONE
                holder.itemView.iconFavourite.visibility=View.VISIBLE
                propertyList[position].is_favourite=0
                functionRemoveFromFav.invoke(propertyList[position].id,position)
            }

            holder.itemView.linearTransparent.setOnClickListener {
                imagePropertyList= ArrayList()
                imagePropertyList.add(propertyList[position].property_priority_image.document)
                StfalconImageViewer.Builder<String>(context,imagePropertyList) { view, image ->
                    Log.e("check", Gson().toJson(image))
                    view.background= ContextCompat.getDrawable(context,R.drawable.shape_placeholder)
                    Glide.with(context)
                        .load(image)
                        .error(R.drawable.shape_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }.show()
            }
        holder.itemView.btnViewDetails.setOnClickListener {
            function.invoke(propertyList[position].id)
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
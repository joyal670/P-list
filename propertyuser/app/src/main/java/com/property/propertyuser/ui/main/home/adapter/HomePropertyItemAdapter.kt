package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_list.Property
import kotlinx.android.synthetic.main.list_home_property_item.view.*


class HomePropertyItemAdapter  (private val context: Context,
                                private val propertyList:List<Property>,
                                private val functionInfo: (Int, String) -> Unit,
                                private val functionMap:(Int)->Unit,
                                private val functionBook:(Int)->Unit,
                                private val functionAddToFav:(Int,Int)->Unit,
                                private val functionRemoveFromFav:(Int,Int)->Unit, )
    : RecyclerView.Adapter<HomePropertyItemAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home_property_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        if(propertyList[position].property_to==0){
            holder.itemView.tvAppartmentText.text=context.getString(R.string.tvAppartmentText)
        }else{
            holder.itemView.tvAppartmentText.text=context.getString(R.string.tvAppartmentSaleText)
        }
        holder.itemView.tvPropertyAmount.text= context.getText(R.string.sar).toString() +" "+propertyList[position].mrp
        holder.itemView.tvPropertyDiscountAmount.text= context.getText(R.string.sar).toString()+" "+propertyList[position].selling_price
        holder.itemView.tvRating.text=propertyList[position].rating
        holder.itemView.tvBedCount.text= propertyList[position].Beds.toString()
        holder.itemView.tvBathCount.text=propertyList[position].Bathroom.toString()
        holder.itemView.tvDiameter.text=propertyList[position].area.toString()
        holder.itemView.ivInfo.setOnClickListener {
            functionInfo.invoke(propertyList[position].id,"")
        }
        if(propertyList[position].documents!=null){
            Glide.with(context).load(propertyList[position].documents[0].document).into(holder.itemView.ivPropertyImage)
        }
        else{
            Glide.with(context).load(R.drawable.ic_image_placeholder_new).into(holder.itemView.ivPropertyImage)
        }
        holder.itemView.ivMap.setOnClickListener {
          functionMap.invoke(position)
        }
        holder.itemView.ivCalender.setOnClickListener {
            functionBook.invoke(position)
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
        holder.itemView.ivShare.setOnClickListener {
            shareFile()
        }
        holder.itemView.ivPropertyImage.setOnClickListener {
            functionInfo.invoke(propertyList[position].id,"")
           // context.startActivity(Intent(context,PropertyImageViewActivity::class.java))
        }
    }
    private fun shareFile() {
        try {
            /*val uri: Uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID.toString() + ".provider",
                File(file)
            )*/
            val share = Intent(Intent.ACTION_SEND)
            share.type = "*/*"
            //share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_TEXT, "passs")
            context.startActivity(Intent.createChooser(share, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
package com.property.propertyuser.ui.main.favorites.adapter

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
import com.property.propertyuser.modal.favorite_list.FaviourateProperty
import com.property.propertyuser.utils.CommonMethods
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.list_home_property_item.view.*
import kotlinx.android.synthetic.main.list_home_property_item.view.tvPropertyAmount
import kotlinx.android.synthetic.main.list_home_property_item.view.tvPropertyDiscountAmount
import kotlinx.android.synthetic.main.list_package_booking_item.view.*


class FavoritePropertyItemAdapter  (private val context: Context,
                                    private val propertyList:List<FaviourateProperty>,
                                    private val functionInfo: (Int, String) -> Unit,
                                    private val functionMap:(Int)->Unit,
                                    private val functionBook:(Int)->Unit,
                                    private val functionAddToFav:(Int,Int)->Unit,
                                    private val functionRemoveFromFav:(Int,Int)->Unit, )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder
    {
        return when (viewtype)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_home_property_item,parent,false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS ->
            {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loader,parent,false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }
    override fun getItemCount(): Int {
        return propertyList.size
    }override fun getItemViewType(position: Int): Int
    {
        var viewtype = propertyList[position].id
        return when(viewtype)
        {//if data is load, returns PROGRESSBAR viewtype.
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            if(propertyList[position].property_to==0){
                holder.itemView.tvAppartmentText.text=context.getString(R.string.property_for_rent)
                holder.itemView.tvPropertyAmount.text= context.getText(R.string.sar).toString() +" "+propertyList[position].rent
                holder.itemView.tvPropertyDiscountAmount.visibility=View.GONE
            }else{
                holder.itemView.tvAppartmentText.text=context.getString(R.string.property_for_sale)
                holder.itemView.tvPropertyAmount.text= context.getText(R.string.sar).toString() +" "+propertyList[position].selling_price
                holder.itemView.tvPropertyDiscountAmount.text= context.getText(R.string.sar).toString()+" "+propertyList[position].mrp
            }
            holder.itemView.tvPropertyDiscountAmount.paintFlags =
                holder.itemView.tvPropertyDiscountAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.tvRating.text=propertyList[position].rating
            holder.itemView.tvBedCount.text= propertyList[position].Beds.toString()
            holder.itemView.tvBathCount.text=propertyList[position].Bathroom.toString()
            holder.itemView.tvDiameter.text=propertyList[position].area.toString()+context?.getString(R.string.sq_m)
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
                functionMap.invoke(propertyList[position].id)
            }
            holder.itemView.ivCalender.setOnClickListener {
                functionBook.invoke(propertyList[position].id)
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
                val stringBuilder = StringBuilder()
               stringBuilder!!.append("Property Name :"+propertyList[position].property_name)
               stringBuilder!!.append("\nProperty Type"+holder.itemView.tvAppartmentText.text)
               stringBuilder!!.append("\nhttps://siaaha.com/property/${propertyList[position].id}")
                Log.e("te",stringBuilder.toString())
               shareTest(stringBuilder.toString(),holder.itemView.ivPropertyImage,context)
            }
            /*holder.itemView.ivPropertyImage.setOnClickListener {
                functionInfo.invoke(propertyList[position].id,"")
                // context.startActivity(Intent(context,PropertyImageViewActivity::class.java))
            }*/
            holder.itemView.linearTransparent.setOnClickListener {

                StfalconImageViewer.Builder<com.property.propertyuser.modal.favorite_list.Document>(context,propertyList[position].documents) { view, image ->
                    Log.e("check", Gson().toJson(image))
                    view.background= ContextCompat.getDrawable(context,R.drawable.shape_placeholder)
                    Glide.with(context)
                        .load(image.document)
                        .error(R.drawable.shape_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }.show()
            }

            holder.itemView.tvPropertyName.text = propertyList[position].property_name
        }
    }
    private fun shareTest(content:String,
                          ivPropertyImage: ImageView,
                          context: Context
    ) {

        Log.e("eee", CommonMethods.getLocalBitmapUri(ivPropertyImage, context).toString())
        val uriImage= CommonMethods.getLocalBitmapUri(ivPropertyImage, context)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_STREAM, uriImage)
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        this.context.startActivity(shareIntent)
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
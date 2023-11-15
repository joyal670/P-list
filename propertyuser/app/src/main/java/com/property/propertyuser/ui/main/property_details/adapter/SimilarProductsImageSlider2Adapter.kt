package com.property.propertyuser.ui.main.property_details.adapter

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
import com.property.propertyuser.modal.property_details.Document
import com.property.propertyuser.modal.property_details.SimilarProperty
import com.property.propertyuser.utils.CommonMethods
import com.property.propertyuser.utils.loadImagesWithGlideExt
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.fragment_similar_products_item.view.*

import kotlinx.android.synthetic.main.list_home_property_item.view.*
import kotlinx.android.synthetic.main.list_home_property_item.view.ivPropertyImage
import kotlinx.android.synthetic.main.list_package_booking_item.view.*
import kotlinx.android.synthetic.main.list_similar_products_item2.view.*


class SimilarProductsImageSlider2Adapter(private val context: Context,
                                         private val similarProperty: List<SimilarProperty>,
                                         private val functionInfoSimilar: (Int) -> Unit,
                                         private val functionMapSimilar:(Int)->Unit,
                                         private val functionBookSimilar:(Int)->Unit,
                                         private val functionAddToFavSimilar:(Int,Int)->Unit,
                                         private val functionRemoveFromFavSimilar:(Int,Int)->Unit,
) : RecyclerView.Adapter<SimilarProductsImageSlider2Adapter.SimilarProductsHolder>() {


    class SimilarProductsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarProductsHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_similar_products_item2, parent, false)
        return SimilarProductsHolder(v)
    }

    override fun getItemCount(): Int {
        return similarProperty.size
    }

    override fun onBindViewHolder(holder: SimilarProductsHolder, position: Int) {
        val similarProperty=similarProperty[position]
        if(similarProperty.documents.isNotEmpty()){
            holder.itemView.ivPropertyImageSimilar.
                loadImagesWithGlideExt(similarProperty.documents[0].document)
        }
        //holder.itemView.tvPropertyName.text = similarProperty.
        if(similarProperty.property_to==0){
            holder.itemView.tvAppartmentTextSimilar.text=context.getString(R.string.property_for_rent)
        }
        else{
            holder.itemView.tvAppartmentTextSimilar.text=context.getString(R.string.property_for_sale)
        }
        holder.itemView.tvPropertyAmountSimilar.text="SAR "+similarProperty.selling_price
        holder.itemView.tvPropertyDiscountAmountSimilar.text="SAR "+similarProperty.mrp
        holder.itemView.tvPropertyDiscountAmountSimilar.paintFlags =
            holder.itemView.tvPropertyDiscountAmountSimilar.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.itemView.tvRatingSimilar.text=similarProperty.rating
        holder.itemView.tvBedCountSimilar.text=similarProperty.Beds.toString()
        holder.itemView.tvBathCountSimilar.text=similarProperty.Bathroom.toString()
        holder.itemView.tvDiameterSimilar.text=similarProperty.area.toString() +"sq. M."
        holder.itemView.iconFavouriteSimilar.isCheckable = similarProperty.is_favourite==1
        if(similarProperty.is_featured==1){
            holder.itemView.ivGreenTagSimilar.visibility=View.VISIBLE
        }else{
            holder.itemView.ivGreenTagSimilar.visibility=View.GONE
        }
        if(similarProperty.is_favourite==1){
            holder.itemView.iconFavouriteSimilar.visibility=View.GONE
            holder.itemView.iconFavouriteSelectedSimilar.visibility=View.VISIBLE
        }else
        {
            holder.itemView.iconFavouriteSelectedSimilar.visibility=View.GONE
            holder.itemView.iconFavouriteSimilar.visibility=View.VISIBLE
        }
        holder.itemView.iconFavouriteSimilar.setOnClickListener {
            holder.itemView.iconFavouriteSimilar.visibility=View.GONE
            holder.itemView.iconFavouriteSelectedSimilar.visibility=View.VISIBLE
            similarProperty.is_favourite=1
            functionAddToFavSimilar.invoke(similarProperty.id,position)
        }
        holder.itemView.iconFavouriteSelectedSimilar.setOnClickListener {
            holder.itemView.iconFavouriteSelectedSimilar.visibility=View.GONE
            holder.itemView.iconFavouriteSimilar.visibility=View.VISIBLE
            similarProperty.is_favourite=0
            functionRemoveFromFavSimilar.invoke(similarProperty.id,position)
        }
        holder.itemView.ivInfoSimilar.setOnClickListener {
            functionInfoSimilar.invoke(similarProperty.id)
        }
        holder.itemView.ivMapSimilar.setOnClickListener {
            functionMapSimilar.invoke(similarProperty.id)
        }
        holder.itemView.ivCalenderSimilar.setOnClickListener {
            functionBookSimilar.invoke(similarProperty.id)
        }
       holder.itemView.ivShareSimilar.setOnClickListener {
           val stringBuilder = StringBuilder()
               stringBuilder!!.append("Property Code :"+similarProperty.property_reg_no)
               stringBuilder!!.append("\nProperty Type"+holder.itemView.tvAppartmentTextSimilar.text)
               stringBuilder!!.append("\nhttps://siaaha.com/property/${similarProperty.id}")
                Log.e("te",stringBuilder.toString())
               shareTest(stringBuilder.toString(),holder.itemView.ivPropertyImageSimilar,context)
       }
        holder.itemView.linearTransparentSimilar.setOnClickListener {
            StfalconImageViewer.Builder<Document>(context,similarProperty.documents) { view, image ->
                Log.e("check", Gson().toJson(image))
                view.background=ContextCompat.getDrawable(context,R.drawable.shape_placeholder)
                Glide.with(context)
                    .load(image.document)
                    .error(R.drawable.shape_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view)
            }.show()
        }
    }
    private fun shareTest(content:String,
                          ivPropertyImage: ImageView,
                          context: Context
    ) {

        Log.e("eee",CommonMethods.getLocalBitmapUri(ivPropertyImage, context).toString())
        val uriImage=CommonMethods.getLocalBitmapUri(ivPropertyImage, context)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_STREAM, uriImage)
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        this.context.startActivity(shareIntent)
    }
}
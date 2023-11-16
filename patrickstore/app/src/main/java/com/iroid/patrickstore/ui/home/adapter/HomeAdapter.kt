package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.home.Slider
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants
import kotlinx.android.synthetic.main.item_festival.view.*
import kotlinx.android.synthetic.main.item_shop.view.*
import kotlinx.android.synthetic.main.recycle_seller_item.view.*

class DealAdapter :
    RecyclerView.Adapter<DealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_deal, parent, false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems() {

        }
    }


}

class FestivalAdapter(private val context: Context, private val slider: List<Slider>) :
    RecyclerView.Adapter<FestivalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_festival, parent, false))
    }

    override fun getItemCount(): Int {
        return slider.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(slider[position],context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(slider: Slider, context: Context) {
            itemView.setOnClickListener {
                if(slider.category== Constants.CONSTANT_SELLER){
                    val intent = Intent(context, ShopDetailsActivity::class.java)
                    intent.putExtra(Constants.INTENT_STORE_ID, slider.sellerId)
                    context.startActivity(intent)
                }
            }
            CommonUtils.setImageBase(context,slider.mainImage.publicUrl,itemView.roundedImageView)
        }
    }


}


class ShopAdapter(private val context: Context, private val shopList: List<SingleSeller>) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_shop, parent, false))
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(shopList[position], context)
        holder.itemView.llShop.setOnClickListener {

            val intent = Intent(context, ShopDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_STORE_ID,shopList[position].id)
            context.startActivity(intent)
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(shopX: SingleSeller, context: Context) {
            CommonUtils.setImageBase(context, shopX.profileImageId.publicUrl, itemView.ivShop)
            itemView.tvStore.text = shopX.sellerName
            itemView.tvDec.text=shopX.description
        }
    }


}

class ShopAdapter1(private val context: Context, private val shopList: List<SingleSeller>) :
    RecyclerView.Adapter<ShopAdapter1.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycle_seller_item, parent, false))
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(shopList[position], context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShopDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_STORE_ID,shopList[position].id)
            context.startActivity(intent)
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(shopX: SingleSeller, context: Context) {
            CommonUtils.setImageBase(context, shopX.profileImageId.publicUrl, itemView.ivStore)
            itemView.tvProductName.text = shopX.sellerName
            itemView.tvDescription.text=shopX.description
        }
    }


}

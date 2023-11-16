package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.home.Banner
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*


class SliderHome(
    private val context: Context,
    private val bannerList: List<Banner>,
    private val onClick:(String)->Unit
) : SliderViewAdapter<SliderHome.SliderAdapterVH>() {
    class SliderAdapterVH(view: View) :
        ViewHolder(view) {

    }




    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val v = LayoutInflater.from(context).inflate(
            R.layout.image_slider_layout_item,
            parent, false)
        return SliderAdapterVH(v)
    }

    override fun getCount(): Int {
        return bannerList.size

    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        CommonUtils.setImageBase(context,bannerList[position].mainImage.publicUrl,viewHolder!!.itemView.iv_auto_image_slider)
        viewHolder!!.itemView.iv_auto_image_slider.setOnClickListener {
            if(bannerList[position].category== Constants.CONSTANT_SELLER){
                val intent = Intent(context, ShopDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_STORE_ID, bannerList[position].sellerId)
                context.startActivity(intent)
            }
        }
        viewHolder.itemView!!.frameBanner.setOnClickListener {
            onClick.invoke(position.toString())
        }
    }

}

package com.iroid.patrickstore.ui.offer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemOfferBannerMiniBinding
import com.iroid.patrickstore.utils.CommonUtils

class OfferMiniAdapter(private val context: Context) :
RecyclerView.Adapter<OfferMiniAdapter.HobbiesViewHolder>() {

    private val bannerList = listOf<String>(
        "http://drive.google.com/uc?export=view&id=1OgN_Pc2iIxVcMgO_KabF8eFN54-5FaTw",
        "http://drive.google.com/uc?export=view&id=1dAWmmiAHDTYqXN2Ht5_1DKQlGr7gXUBG"
    )
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbiesViewHolder {
    val view = ItemOfferBannerMiniBinding.inflate(LayoutInflater.from(parent.context) , parent,false)
    return HobbiesViewHolder(view)
}

override fun onBindViewHolder(holder: HobbiesViewHolder, position: Int) {
    CommonUtils.setImageBase(context,bannerList[position],holder.viewBinding.image)

}

inner class HobbiesViewHolder(var viewBinding: ItemOfferBannerMiniBinding) : RecyclerView.ViewHolder(viewBinding.root) {
}

override fun getItemCount(): Int {
    return bannerList.size
}

}
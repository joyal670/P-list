package com.iroid.patrickstore.ui.offer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemOfferBannerBinding
import com.iroid.patrickstore.utils.CommonUtils

class OfferMainAdapter(private val context: Context) :
    RecyclerView.Adapter<OfferMainAdapter.OfferMainViewHolder>() {
    private val bannerList = listOf<String>(
        "http://drive.google.com/uc?export=view&id=1lbNSEE5aVoR3a0opZS_BdwmIu3mbBfqk",
        "http://drive.google.com/uc?export=view&id=1fq4epwZBzJ7Sk3AUeae63suenRRkbd9l",
        "http://drive.google.com/uc?export=view&id=1lbNSEE5aVoR3a0opZS_BdwmIu3mbBfqk"
    )

    class OfferMainViewHolder(var binding: ItemOfferBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferMainViewHolder {
        val binding =
            ItemOfferBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferMainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onBindViewHolder(holder: OfferMainViewHolder, position: Int) {
        CommonUtils.setImageBase(context, bannerList[position], holder.binding.ivOffer)
    }
}
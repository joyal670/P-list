package com.iroid.patrickstore.ui.offer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemBrandBinding
import com.iroid.patrickstore.model.festval_offers.Seller
import com.iroid.patrickstore.ui.offer.modal.Brand
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class FeaturedBrandAdapter(private val context: Context, private val sellers: List<Seller>) :
    RecyclerView.Adapter<FeaturedBrandAdapter.FeaturedViewHolder>() {
    private val brandList: List<Brand> = listOf(
        Brand(
            "http://drive.google.com/uc?export=view&id=1mk7Dk9efkIKQukaCvYPQdUN4a_No1t1H",
            "http://drive.google.com/uc?export=view&id=17WCQ75_XSfhDj_0q6MatnkvUWxgviuxl"
        ),
        Brand(
            "http://drive.google.com/uc?export=view&id=1tgU8Wqc3UXTEQwbymuD-C0STQKcemEcV",
            "http://drive.google.com/uc?export=view&id=1HlheuUlzRa0vkaxkEaCwd2s61Xy8FtUy"
        )
    )

    class FeaturedViewHolder(var binding: ItemBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val binding = ItemBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sellers.size
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        CommonUtils.setImageBase(
            context,
            sellers[position].mainImage.publicUrl,
            holder.binding.ivBanner
        )
        holder.binding.ivBanner.setOnClickListener {
            val intent = Intent(context, ShopDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_STORE_ID, sellers[position].sellerId.id)
            context.startActivity(intent)

        }

    }
}

package com.iroid.patrickstore.ui.offer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemOfferCampignBinding
import com.iroid.patrickstore.model.festval_offers.Product
import com.iroid.patrickstore.ui.offer.modal.Brand
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class CampignAdapter(private val context: Context,private val products: List<Product>): RecyclerView.Adapter<CampignAdapter.CampignViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampignViewHolder {
        val binding =
            ItemOfferCampignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampignViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: CampignViewHolder, position: Int) {
        CommonUtils.setImageBase(context,products[position].mainImage.publicUrl,holder.binding.ivOffer)
        holder.binding.ivOffer.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_PRODUCT_ID,products[position].id)
            context!!.startActivity(intent)
        }

    }

    class CampignViewHolder(var binding: ItemOfferCampignBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}

package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemOfferBinding
import com.iroid.patrickstore.model.home.Adds
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class AddAdapter(private val context: Context, private val slider: List<Adds>) :
    RecyclerView.Adapter<AddAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemOfferBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return slider.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(slider[position],context)

    }

  inner  class ViewHolder(var binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(slider: Adds, context: Context) {
            binding.roundedImageView.setOnClickListener {
                if(slider.category==Constants.CONSTANT_SELLER){
                    val intent = Intent(context, ShopDetailsActivity::class.java)
                    intent.putExtra(Constants.INTENT_STORE_ID, slider.sellerId)
                    context.startActivity(intent)
                }
            }
            CommonUtils.setImageBase(this@AddAdapter.context,slider.mainImage.publicUrl,binding.roundedImageView)
        }
    }


}

package com.iroid.patrickstore.ui.shop_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.ItemFoodBinding
import com.iroid.patrickstore.databinding.ItemShopBinding
import com.iroid.patrickstore.utils.Constants

class ShopProductAdapter(
    private val context: Context,
    private val type: Boolean,
    private val productType: Int,
    private val function: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(type){
            return ProductViewHolder(ItemFoodBinding.inflate(LayoutInflater.from(context),parent,false))
        }else{
            return ShopViewHolder(ItemShopBinding.inflate(LayoutInflater.from(context),parent,false))
        }

    }

    override fun getItemCount(): Int {
       return 10
    }


    private inner class ShopViewHolder(var  binding: ItemShopBinding):RecyclerView.ViewHolder(binding.root){
          fun bind() {

        }

    }
    private inner class ProductViewHolder(var  binding: ItemFoodBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(productType: Int) {
//            if(productType==0){
//                binding.textView5.text="Alfam"
//            }else{
//                binding.ivShopItem.setImageResource(R.drawable.dummy_mouse)
//            }
            itemView.setOnClickListener {
                function.invoke(productType)
            }
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(type){
            (holder as ProductViewHolder).bind(productType)
        }else{
            (holder as ShopViewHolder).bind()
        }

    }
}
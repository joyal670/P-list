package com.iroid.patrickstore.ui.dailydeal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.*
import com.iroid.patrickstore.model.daily_deal.DailyDealType
import com.iroid.patrickstore.model.daily_deal.ProductDailyDeal
import com.iroid.patrickstore.model.daily_deal.SellerImageX
import com.iroid.patrickstore.utils.CommonUtils


class DailyDealAdapter(private val context: Context, private  val dailyDeal: List<DailyDealType>): RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    companion object {
        const val VIEW_TYPE_STORE = 1
        const val VIEW_TYPE_PRODUCT = 2

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_STORE -> {
                StoreViewHolder(ItemShopHeaderBinding.inflate(LayoutInflater.from(context),parent,false))
            }
            VIEW_TYPE_PRODUCT -> {
                ProductViewHolder(ItemDealContentBinding.inflate(LayoutInflater.from(context),parent,false))

            }
            else -> {
                StoreViewHolder(ItemShopHeaderBinding.inflate(LayoutInflater.from(context),parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(dailyDeal[position].type==1){
            (holder as StoreViewHolder).bind(context,dailyDeal[position].sellerName,dailyDeal[position].sellerImage)
        }else{
            (holder as ProductViewHolder).bind(context,dailyDeal[position].products)

        }
    }

    override fun getItemCount(): Int {
        return dailyDeal.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(dailyDeal[position].type==1){
            VIEW_TYPE_STORE
        }else{
            VIEW_TYPE_PRODUCT

        }
    }


    private inner class ProductViewHolder(var binding: ItemDealContentBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, productList: List<ProductDailyDeal>){
            binding.rvDealProduct.layoutManager = GridLayoutManager(context, 2)
            binding.rvDealProduct.adapter =
                DailyDealItemAdapter(context)

            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
            binding.rvDealProduct.layoutAnimation = controller
            binding.rvDealProduct.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.rvDealProduct.adapter = DailyDealProductAdapter(context, productList!!) { id, qty ->
//            functionAddToCart(
//                id,
//                qty
//            )
            }
        }

    }
    private inner class StoreViewHolder(var  binding: ItemShopHeaderBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, sellerName: String, sellerImage: SellerImageX){
            CommonUtils.setImageBase(context,sellerImage.publicUrl,binding.ivShop)
            binding.tvShopName.text=sellerName
        }
    }

}

package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemFoodBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class ProductAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private val functionAddToCart: (String,String) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItems(productList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_PRODUCT_ID,productList[position].id)
            context!!.startActivity(intent)
        }
    }

    inner class ProductViewHolder(var binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(product: Product) {
            binding.tvOfferPrice.paintFlags = binding.tvOfferPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvProductName.text = product.name
            binding.tvPrice.text = product.actualPrice.toString()
            binding.tvOfferPrice.text = product.displayPrice.toString()
            binding.ratingBar.rating=product.averageRating
            try{
                if(product.imgUrl.isNotEmpty()){
                    CommonUtils.setImageBase(context!!, product.imgUrl[0].publicUrl, binding.ivProduct)
                }
            }catch (ex:Exception){

            }

            binding.btnAddToCart.setOnClickListener {
                binding.constraintQty.visibility= View.VISIBLE
                binding.btnAddToCart.visibility=View.GONE
                functionAddToCart.invoke(product.id,"1")
            }
            var quantity: Int? = binding.tvQuantity.text.toString().toInt()
            binding.ivPlus.setOnClickListener {
                quantity = Integer.parseInt(binding.tvQuantity.text.toString())
                if (quantity!! < 100) {
                    quantity = quantity!! + 1
                    binding.tvQuantity.text = quantity.toString()
                }
                functionAddToCart.invoke(product.id, quantity.toString())
            }
            binding.ivMinus.setOnClickListener {
                quantity = Integer.parseInt(binding.tvQuantity.text.toString())
                if (quantity != 1) {
                    quantity = quantity!! - 1
                    binding.tvQuantity.text = quantity.toString()
                    functionAddToCart.invoke(product.id, quantity.toString())

                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemFoodBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }
}

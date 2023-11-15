package com.iroid.patrickstore.ui.cart.confirm_order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.RecycleCartItemsBinding
import com.iroid.patrickstore.model.order_summary.CartItem
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.toCovertOffer

class CartItemsAdapter(
    private val cartItems: List<CartItem>,
    private val context: Context
) : RecyclerView.Adapter<CartItemsAdapter.ConfirmOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmOrderViewHolder {
        return ConfirmOrderViewHolder(
            RecycleCartItemsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: ConfirmOrderViewHolder, position: Int) {
        holder.setView(cartItems[position],context)
    }

    inner class ConfirmOrderViewHolder(var binding: RecycleCartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(cartItem: CartItem, context: Context) {
            binding.tvProductName.text=cartItem.product.name
            binding.tvQuantity.text=cartItem.quantity.toString()
            binding.tvRate.text=CommonUtils.getCurrencyFormat(cartItem.product.actualPrice.toString())
            binding.tvCrossedRate.text=CommonUtils.getCurrencyFormat(cartItem.product.displayPrice.toString())
            binding.tvOffPercentage.text=cartItem.product.displayPrice.toString().toCovertOffer(cartItem.product.actualPrice.toString())
            binding.tvModel.text=cartItem.product.description
            CommonUtils.setImageBase(context,cartItem.product.imgUrl[0].publicUrl,binding.ivImage)
        }
    }


}

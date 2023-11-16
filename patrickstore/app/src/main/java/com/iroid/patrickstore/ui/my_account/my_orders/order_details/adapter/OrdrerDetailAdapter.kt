package com.iroid.patrickstore.ui.my_account.my_orders.order_details.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.LayoutMyCartRecycleItemBinding
import com.iroid.patrickstore.model.order_detail.CartItem
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.toCovertOffer

class OrdrerDetailAdapter(
    private val context: Context,
    private val cartItems: List<CartItem>,
    private val function: (String) -> Unit,
    private val deliveryStatus:Boolean
) : RecyclerView.Adapter<OrdrerDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdrerDetailAdapter.ViewHolder {
        return ViewHolder(
            LayoutMyCartRecycleItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdrerDetailAdapter.ViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class ViewHolder(var binding: LayoutMyCartRecycleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(position: Int) {
            if(deliveryStatus){
                binding.layoutCart.btnReturnOrder.visibility=View.VISIBLE
            }else{
                binding.layoutCart.btnReturnOrder.visibility=View.GONE
            }
            if (cartItems[position].product.imgUrl.isNotEmpty()) {
                CommonUtils.setImageFitCenter(
                    context,
                    cartItems[position].product.imgUrl[0].publicUrl,
                    binding.layoutCart.imgProduct
                )
            }
            if(cartItems[position].showReturn){
                binding.layoutCart.btnReturnOrder.visibility=View.VISIBLE
            }else{
                binding.layoutCart.btnReturnOrder.visibility=View.GONE
            }
            binding.layoutCart.tvProduct.text = cartItems[position].product.name
            binding.layoutCart.tvModel.text = cartItems[position].product.model
            binding.layoutCart.tvPrice.text =
                context.getString(R.string.rupee_symbol) + cartItems[position].product.actualPrice.toString()
            binding.layoutCart.tvDiscount.text =
                context.getString(R.string.rupee_symbol) + cartItems[position].product.displayPrice.toString()
            binding.layoutCart.tvOffer.text =
                cartItems[position].product.displayPrice.toString().toCovertOffer(cartItems[position].product.actualPrice.toString())

            //Setting Quantity
            var quantity: Int? = cartItems[position].quantity
            binding.layoutCart.tvQuantity.text = quantity.toString()
            binding.layoutCart.tvPlus.setOnClickListener {
                quantity = Integer.parseInt(binding.layoutCart.tvQuantity.text.toString())
                if (quantity!! < 100) {
                    quantity = quantity!! + 1
                    binding.layoutCart.tvQuantity.text = quantity.toString()
                }
            }
            binding.layoutCart.btnReturnOrder.setOnClickListener {
                function.invoke(cartItems[position].product.id)
            }
            binding.layoutCart.tvMinus.setOnClickListener {
                quantity = Integer.parseInt(binding.layoutCart.tvQuantity.text.toString())
                if (quantity != 1) {
                    quantity = quantity!! - 1
                    binding.layoutCart.tvQuantity.text = quantity.toString()
                }
            }
            binding.layoutCart.textQuantity.visibility= View.GONE
            binding.layoutCart.constraintQty.visibility=View.GONE
            binding.layoutCart.tvRemove.visibility=View.GONE
            binding.layoutCart.tvLater.visibility=View.GONE

            //Setting Size Spinner
            val adapter = ArrayAdapter.createFromResource(
                context,
                R.array.size_list, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.layoutCart.spinnerSize.adapter = adapter
            binding.layoutCart.tvSize.text = binding.layoutCart.spinnerSize.selectedItem.toString()


        }
    }
}

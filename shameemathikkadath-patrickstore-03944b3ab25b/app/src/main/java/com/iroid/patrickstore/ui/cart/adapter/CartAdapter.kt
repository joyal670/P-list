package com.iroid.patrickstore.ui.cart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.LayoutMyCartRecycleItemBinding
import com.iroid.patrickstore.model.cart_listing.CartItem
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.toCovertOffer
import com.iroid.patrickstore.utils.toPrice

class CartAdapter(
    private val context: Context,
    private val cartItems: List<CartItem>,
    private val functionRemoveSingleItem: (String) -> Unit,
    private val functionAddToWishList: (String) -> Unit,
    private val functionUpdateCart: (String,String) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        return ViewHolder(
            LayoutMyCartRecycleItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.bindItems(position,functionUpdateCart)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class ViewHolder(var binding: LayoutMyCartRecycleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(position: Int, functionUpdateCart: (String, String) -> Unit) {
            //Setting StoreData
            if (!cartItems[position].sellerId.profileImageId.toString().isNullOrEmpty()) {
                CommonUtils.setImageFitCenter(
                    context,
                    cartItems[position].sellerId.profileImageId.toString(),
                    binding.layoutStore.imgStore
                )
            }
            binding.layoutStore.tvShopName.text = cartItems[position].sellerId.storeName
            val storeAddress: String =
                cartItems[position].sellerId.addressDetails.address + ", " +
                        cartItems[position].sellerId.addressDetails.location + ", " +
                        cartItems[position].sellerId.addressDetails.district + ", " +
                        cartItems[position].sellerId.addressDetails.state + ", " +
                        cartItems[position].sellerId.addressDetails.pincode
            binding.layoutStore.tvShopAddress.text = storeAddress

            //Setting CartData
            if (cartItems[position].product.imgUrl.isNotEmpty()) {
                CommonUtils.setImageFitCenter(
                    context,
                    cartItems[position].product.imgUrl[0].publicUrl,
                    binding.layoutCart.imgProduct
                )
            }
            binding.layoutCart.tvProduct.text = cartItems[position].product.name
            binding.layoutCart.tvModel.text = cartItems[position].product.model
            binding.layoutCart.tvPrice.text =
                context.getString(R.string.rupee_symbol) + cartItems[position].product.actualPrice.toString().toDouble().toPrice()
            binding.layoutCart.tvDiscount.text =
                context.getString(R.string.rupee_symbol) + cartItems[position].product.displayPrice.toString().toDouble().toPrice()
            binding.layoutCart.tvOffer.text =cartItems[position].product.displayPrice.toString().toCovertOffer(cartItems[position].product.actualPrice.toString())

            //Setting Quantity
            var quantity: Int? = cartItems[position].quantity
            binding.layoutCart.tvQuantity.text = quantity.toString()
            binding.layoutCart.tvPlus.setOnClickListener {
                quantity = Integer.parseInt(binding.layoutCart.tvQuantity.text.toString())
                if (quantity!! < 100) {
                    quantity = quantity!! + 1
                    binding.layoutCart.tvQuantity.text = quantity.toString()
                }
                functionUpdateCart.invoke(cartItems[position].product.id,quantity.toString())
            }
            binding.layoutCart.tvMinus.setOnClickListener {
                quantity = Integer.parseInt(binding.layoutCart.tvQuantity.text.toString())
                if (quantity != 1) {
                    quantity = quantity!! - 1
                    binding.layoutCart.tvQuantity.text = quantity.toString()
                    functionUpdateCart.invoke(cartItems[position].product.id,quantity.toString())

                }
            }

            //Setting Size Spinner
            val adapter = ArrayAdapter.createFromResource(
                context,
                R.array.size_list, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.layoutCart.spinnerSize.adapter = adapter
            binding.layoutCart.tvSize.text = binding.layoutCart.spinnerSize.selectedItem.toString()

            binding.layoutCart.tvRemove.setOnClickListener {
                functionRemoveSingleItem.invoke(cartItems[position].product.id)
            }

            binding.layoutCart.tvLater.setOnClickListener {
                functionAddToWishList.invoke(cartItems[position].product.id)
            }
        }
    }
}

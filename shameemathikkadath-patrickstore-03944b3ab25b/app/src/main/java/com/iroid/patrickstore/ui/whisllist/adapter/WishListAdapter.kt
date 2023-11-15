package com.iroid.patrickstore.ui.whisllist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.ItemWhishlistBinding
import com.iroid.patrickstore.model.wishlist_listing.Item

class WishListAdapter(
    private val context: Context,
    private val wishlistItems: List<Item>,
    private val functionRemoveSingleItem: (String) -> Unit,
    private val functionAddToCart: (String, String) -> Unit,
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWhishlistBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return wishlistItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position)
    }

    inner class ViewHolder(var binding: ItemWhishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(position: Int) {

            binding.tvProduct.text = wishlistItems[position].name
            binding.tvPrice.text =
                context.getString(R.string.rupee_symbol) + wishlistItems[position].offerPrice.toString()
            binding.tvDiscount.text =
                context.getString(R.string.rupee_symbol) + wishlistItems[position].actualPrice.toString()
            binding.tvOffer.text =
                wishlistItems[position].shopDiscount.toString() + context.getString(
                    R.string.discount_percentage
                )

            binding.tvRemove.setOnClickListener {
                functionRemoveSingleItem.invoke(wishlistItems[position].id)
            }

            binding.tvLater.setOnClickListener {
                functionAddToCart.invoke(
                    wishlistItems[position].id,
                    wishlistItems[position].minQunatity.toString()
                )
            }
        }
    }
}
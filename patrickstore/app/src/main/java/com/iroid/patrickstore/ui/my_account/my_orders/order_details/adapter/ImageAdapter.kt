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
import com.iroid.patrickstore.databinding.RecycerImageUploadItemBinding
import com.iroid.patrickstore.model.order_detail.CartItem
import com.iroid.patrickstore.utils.CommonUtils

class ImageAdapter(
    private val context: Context,
    private val cartItems: List<CartItem>,
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        return ViewHolder(
            RecycerImageUploadItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class ViewHolder(var binding: RecycerImageUploadItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(position: Int) {

        }
    }
}

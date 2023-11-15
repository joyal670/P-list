package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemFoodBinding
import com.iroid.patrickstore.databinding.ItemServiceOrderBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class

ServiceAdapter(
    private val context: Context,
    private val function: (String,String) -> Unit
) :
    RecyclerView.Adapter<ServiceAdapter.ServiceOrderViewHolder>() {

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ServiceOrderViewHolder, position: Int) {

    }

    inner class ServiceOrderViewHolder(var binding: ItemServiceOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems() {


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceOrderViewHolder {
        return ServiceOrderViewHolder(
            ItemServiceOrderBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }
}

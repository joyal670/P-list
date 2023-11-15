package com.iroid.patrickstore.ui.service_order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemServiceOrderBinding
import com.iroid.patrickstore.model.service.service_order.ItemServiceOrder
import com.iroid.patrickstore.utils.Toaster

class ServiceOrderAdapter(
    private val context: Context,
    private val items: List<ItemServiceOrder>,
    private val function: (String) -> Unit
) :
    RecyclerView.Adapter<ServiceOrderAdapter.ServiceOrderViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ServiceOrderViewHolder, position: Int) {
        holder.bindItems(items[position])
        holder.binding.tvViewDetails.setOnClickListener {
            if(items[position].requeststatus=="notViewed"){
                Toaster.showToast(context,"Seller approval pending",Toaster.State.SUCCESS,Toast.LENGTH_LONG)
            }else{
                function.invoke(items[position].id)
            }

        }
    }

    inner class ServiceOrderViewHolder(var binding: ItemServiceOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(itemServiceOrder: ItemServiceOrder) {
            binding.tvServiceName.text = itemServiceOrder.serviceId.serviceName
            binding.tvQty.text = itemServiceOrder.quantity.toString()
            if (itemServiceOrder.isConvertedToOrder){
                binding.tvStatus.text = "Order Placed"
            }else{
                binding.tvStatus.text = itemServiceOrder.requeststatus
            }

            binding.tvPriceQuote.text=itemServiceOrder.rate.toString()

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

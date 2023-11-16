package com.iroid.patrickstore.ui.service_order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemPaymentHistoryBinding
import com.iroid.patrickstore.model.service.service_order_detail.ResponseHistory
import com.iroid.patrickstore.utils.CommonUtils

class ServicePaymentAdapter(
    private val context: Context,
    private val historyPayment: List<ResponseHistory>
) :
    RecyclerView.Adapter<ServicePaymentAdapter.ServiceOrderViewHolder>() {

    override fun getItemCount(): Int {
        return historyPayment.size
    }

    override fun onBindViewHolder(holder: ServiceOrderViewHolder, position: Int) {
        holder.bindItems(historyPayment[position])
    }

    inner class ServiceOrderViewHolder(var binding: ItemPaymentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(responseHistory: ResponseHistory) {
            if(responseHistory.isCustomer){
                binding.tvSellerPrice.text="Customer Price"
            }else{
                binding.tvSellerPrice.text="Seller Price"
            }
            binding.tvPrice.text=CommonUtils.getCurrencyFormat(responseHistory.rate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceOrderViewHolder {
        return ServiceOrderViewHolder(
            ItemPaymentHistoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }
}

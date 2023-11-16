package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.databinding.ItemPaymentHistoryBinding

class PaymentHistoryAdapter(
    private var activity: Activity,
    private var selectedItem: (String) -> Unit,
) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHold>() {

    private var context: Context? = null

    var paymentList = arrayOf("Token", "Advance", "Rent")
    var paymentList1 = arrayOf("Verify", "Paid/View", "Decline/View")

    class ViewHold(var binding: ItemPaymentHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = ItemPaymentHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvType.text = paymentList[position]
        holder.binding.tvStatus.text = paymentList1[position]
        holder.binding.tvStatus.paintFlags =  holder.binding.tvStatus.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        holder.binding.tvStatus.setOnClickListener {
            selectedItem.invoke(paymentList1[position])
        }
    }

}
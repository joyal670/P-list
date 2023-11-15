package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemAmenitiesHomeBinding
import com.ncomfortsagent.databinding.ItemPaymentSplitUpBinding

class PaymentSplitUpAdapter(
    private var activity: Activity,
    private var paymentSplitUpArrayList:ArrayList<String>,
    private var selectedItem: (Int) -> Unit,
) : RecyclerView.Adapter<PaymentSplitUpAdapter.ViewHold>() {

    private var context: Context? = null

    var paymentRentList = arrayOf("Token", "Advance", "Rent")
    var paymentSaleList = arrayOf("Token", "Advance", "Selling Price")

    class ViewHold(var binding: ItemPaymentSplitUpBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = ItemPaymentSplitUpBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return paymentSplitUpArrayList.size
    }


    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvType.text = paymentRentList[position]

        holder.binding.tvAmount.text = paymentSplitUpArrayList[position]



       /* holder.binding.tvStatus.paintFlags =  holder.binding.tvStatus.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        holder.binding.tvStatus.setOnClickListener {
            selectedItem.invoke(0)
        }*/
    }

}
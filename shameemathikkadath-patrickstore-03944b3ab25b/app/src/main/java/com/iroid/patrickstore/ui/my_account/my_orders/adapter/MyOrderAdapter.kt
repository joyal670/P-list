package com.iroid.patrickstore.ui.my_account.my_orders.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.RecycleOrderProductItemBinding
import com.iroid.patrickstore.model.my_orders.Item
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.recycle_order_product_item.view.*
import java.time.Instant
import java.util.*

class MyOrderAdapter(
    private val context: Context,
    private val orderList: List<Item>,
    private val function: (String) -> Unit,
) : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderAdapter.ViewHolder {
        return ViewHolder(
            RecycleOrderProductItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyOrderAdapter.ViewHolder, position: Int) {
        holder.bindItems(orderList[position],function)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(var binding: RecycleOrderProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bindItems(item: Item, function: (String) -> Unit) {
            binding.tvOrderId.text=item.orderId.toString()
            val instant=Instant.parse(item.dateOfPurchase)
            val d: Date = Date.from(instant)
            binding.tvDate.text= "Order Date: ${DateFormat.format("yyyy-MM-dd hh:mm:ss a", d)}"

            binding.cardProdcut.setOnClickListener {
                function.invoke(item._id)
            }
            val productItems=StringBuilder()
            item.cartItems.forEach {
                try{
                    productItems.append(it.product.name)
                    productItems.append("   ")
                    productItems.append("Qty: ")
                    productItems.append(it.quantity)
                    productItems.append("   ")
                    productItems.append(CommonUtils.getCurrencyFormat(it.price.toString()))
                    productItems.append("\n")
                }catch (ex:Exception){

                }

            }

            binding.tvNoOfItems.text=productItems.toString()
        }
    }
}

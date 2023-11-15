package com.iroid.patrickstore.ui.my_account.reward_points.cash_back.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.cashback.Item
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.recycle_cash_back_item.view.*

class CashBackItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<CashBackItemAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_cash_back_item, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

//            holder.itemView.tvModel.text= items[position].orderId.orderId.toString()
            if(items[position].type=="product"){
                holder.itemView.tvQuantity.text="Product Order"
            }else{
                holder.itemView.tvQuantity.text="Service Order"
            }
            holder.itemView.textView40.text="Cashback amount ${CommonUtils.getCurrencyFormat(items[position].cashBackAmount.toString())}"

            holder.itemView.tvQuantity
            if(items[position].isEarnedCashBack){
                holder.itemView.tvPending.visibility=View.GONE
                holder.itemView.tvStatus.visibility=View.VISIBLE
            }else{
                holder.itemView.tvPending.visibility=View.VISIBLE
                holder.itemView.tvStatus.visibility=View.GONE
            }



    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setView(){

        }
    }

}

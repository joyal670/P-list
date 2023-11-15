package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.Extra
import com.proteinium.proteiniumdietapp.utils.toPriceRound
import kotlinx.android.synthetic.main.item_extras.view.*


class ExtraAdapter(
    private val extras: List<Extra>
) : RecyclerView.Adapter<ExtraAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_extras, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return extras.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        holder.itemView.tvName.text = extras[index].name + " " + extras[index].quantity
        holder.itemView.tvPrice.text = "${context!!.getString(R.string.kwd)+  " " + extras[index].amount.toDouble().toPriceRound()}"
        //stholder.itemView.tvQty.text = "Quantity : " + extras[index].quantity
        if(index==extras.size-1){
            holder.itemView.viewLine.visibility=View.GONE

        }
    }
}
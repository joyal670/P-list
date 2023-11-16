package com.iroid.patrickstore.ui.dailydeal.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_service_details.view.*
import kotlinx.android.synthetic.main.activity_service_details.view.tvCrossedRate
import kotlinx.android.synthetic.main.activity_service_details.view.tvRupeeSymbol
import kotlinx.android.synthetic.main.deal_product_item.view.*

class DailyDealItemAdapter(private val context: Context): RecyclerView.Adapter<DailyDealItemAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_product_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupViews(context)
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView){


        fun setupViews(context: Context){
            itemView.tvRupeeSymbol.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tvCrossedRate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.cardProduct.setOnClickListener {
                context.startActivity(
                    Intent(context, ProductDetailActivity::class.java)
                )

            }
        }

    }
}
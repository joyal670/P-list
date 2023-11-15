package com.iroid.patrickstore.ui.search.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.deal_product_item.view.*

class SearchAdapter(private val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_product_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
       return 10
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupViews()
        holder.itemView.cardProduct.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailActivity::class.java))
        }
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setupViews(){
            itemView.tvRupeeSymbol.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tvCrossedRate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}

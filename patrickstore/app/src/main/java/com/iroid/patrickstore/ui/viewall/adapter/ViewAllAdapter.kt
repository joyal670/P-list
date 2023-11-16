package com.iroid.patrickstore.ui.viewall.adapter

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

class ViewAllAdapter(private val context: Context) : RecyclerView.Adapter<ViewAllAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_product_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
       return 10
    }



    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setupViews(){
            itemView.tvRupeeSymbol.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tvCrossedRate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun onBindViewHolder(holder: ViewAllAdapter.ViewHold, position: Int) {
        holder.setupViews()
        holder.itemView.cardProduct.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailActivity::class.java))
        }
    }
}

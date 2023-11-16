package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.item_food.view.*

class ProductAdpater(private  val context: Context):
    RecyclerView.Adapter<ProductAdpater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailActivity::class.java
                )
            )
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems() {

        }
    }


}
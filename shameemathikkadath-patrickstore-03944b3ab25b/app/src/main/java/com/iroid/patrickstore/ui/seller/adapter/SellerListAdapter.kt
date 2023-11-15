package com.iroid.patrickstore.ui.seller.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import kotlinx.android.synthetic.main.recycle_seller_item.view.*

class SellerListAdapter(private val context: Context): RecyclerView.Adapter<SellerListAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_seller_item, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
       return 10
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupView()
        holder.itemView.itemSeller.setOnClickListener {
            context.startActivity(Intent(context,ShopDetailsActivity::class.java    ))
        }
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setupView(){

        }
    }
}
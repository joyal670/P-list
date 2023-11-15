package com.iroid.patrickstore.ui.shop_details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R

class ShopReviewAdapter: RecyclerView.Adapter<ShopReviewAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_review_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupViews()
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setupViews(){

        }
    }
}
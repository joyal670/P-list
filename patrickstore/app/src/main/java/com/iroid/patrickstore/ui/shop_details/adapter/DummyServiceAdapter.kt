package com.iroid.patrickstore.ui.shop_details.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.shop_details.services.service_details.ServiceDetailsActivity
import kotlinx.android.synthetic.main.shop_product_item.view.*

class DummyServiceAdapter(private val context:Context): RecyclerView.Adapter<DummyServiceAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dummy_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
       return 8
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setViews()

    }


    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setViews(){

        }
    }


}
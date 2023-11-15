package com.iroid.patrickstore.ui.home.adapter.navadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.service.ServiceCategory
import kotlinx.android.synthetic.main.item_side_category.view.*

class ShopCategoryListAdapter(
    private val serviceCategory: List<ServiceCategory>,
    private val function: (String?, String?) -> Unit
) : RecyclerView.Adapter<ShopCategoryListAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_side_category, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return serviceCategory.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupView(serviceCategory[position].name)
        holder.itemView.setOnClickListener {
            function.invoke(
                serviceCategory[position].id,
                serviceCategory[position].name
            )
        }
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setupView(name: String) {
            itemView.tvCategory.text = name
        }
    }
}

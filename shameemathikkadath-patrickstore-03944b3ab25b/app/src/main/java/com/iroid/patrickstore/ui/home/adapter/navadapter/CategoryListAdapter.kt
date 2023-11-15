package com.iroid.patrickstore.ui.home.adapter.navadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.all_categories.AllCategories
import kotlinx.android.synthetic.main.item_side_category.view.*

class CategoryListAdapter(private val allCategories: List<AllCategories>) : RecyclerView.Adapter<CategoryListAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_side_category, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
       return allCategories.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setupView(allCategories[position].name)
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setupView(s: String) {
            itemView.tvCategory.text=s.toString()
        }


    }
}
package com.iroid.patrickstore.ui.offer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemCategoriesBinding
import com.iroid.patrickstore.ui.allcategories.CategoriesActivity
import com.iroid.patrickstore.ui.viewall.ViewAllActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants

class CategoryAdapter(
    private val context: Context,
    private val categoryList: List<com.iroid.patrickstore.model.festval_offers.Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(var binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.tvCategory.text = categoryList[position].category
        if (categoryList.isNotEmpty()) {
            CommonUtils.setImageBase(
                context,
                categoryList[position].mainImage.publicUrl,
                holder.binding.ivCategory
            )
        }
        holder.binding.ivCategory.setOnClickListener {
            val intent = Intent(context, ViewAllActivity::class.java)
            intent.putExtra("id",categoryList[position].category)
            intent.putExtra("category_name",categoryList[position].category)
            intent.putExtra("type",categoryList[position].type)
            context.startActivity(intent)
        }

    }
}

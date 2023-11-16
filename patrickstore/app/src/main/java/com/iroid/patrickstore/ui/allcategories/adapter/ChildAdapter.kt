package com.iroid.patrickstore.ui.allcategories.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemRowBinding
import com.iroid.patrickstore.model.all_categories.AllCategories
import com.iroid.patrickstore.model.all_categories.ChildrenCategories

class ChildAdapter(private val delegate: Delegate) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

  private val items: ArrayList<ChildrenCategories> = arrayListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
    val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ChildViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
    with(holder.binding) {
      rowTitle.text = items[position].name
      root.setOnClickListener {
        delegate.onRowItemClick(items[position].isPerishable,items[position]._id, root.context)
      }
    }
  }

  fun addItemList(itemList: List<ChildrenCategories>) {
    items.clear()
    items.addAll(itemList)
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  class ChildViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

  interface Delegate {
    fun onRowItemClick(position: Boolean, id: String, context: Context)
  }
}
package com.iroid.patrickstore.ui.allcategories.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.ItemSectionBinding
import com.iroid.patrickstore.model.all_categories.AllCategories

class ParentAdapter(
  context: Context,
  private val categories: List<AllCategories>,
  private val function: (String,Boolean) -> Unit
) :
  RecyclerView.Adapter<ParentAdapter.ParentViewHolder>(), ChildAdapter.Delegate {
  private val childAdapter = ChildAdapter(this)
  private  var isLoaded=false
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
    val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ParentViewHolder(binding).apply {
      with(binding.root) {
        setOnClickListener {
          isLoaded=true
          val position =
            adapterPosition.takeIf { position -> position != RecyclerView.NO_POSITION }
              ?: return@setOnClickListener
          val item = categories[position]
          item.expanded = !item.expanded
          notifyItemChanged(position)

        }
        parentLayoutResource = R.layout.item_section_parent
        secondLayoutResource = R.layout.item_section_child
        duration = 200L
        secondLayout.findViewById<RecyclerView>(R.id.recyclerViewChild).adapter = childAdapter
      }
    }
  }

  override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
    val sectionItem = categories[position]
    with(holder.binding.expandableLayout) {
      if (sectionItem.expanded) {
        parentLayout.findViewById<TextView>(R.id.title).setTypeface(null, Typeface.BOLD);
        expand()
      } else {
        parentLayout.findViewById<TextView>(R.id.title).setTypeface(null, Typeface.NORMAL);
        collapse()
      }
      parentLayout.findViewById<TextView>(R.id.title).text = sectionItem.name
      childAdapter.addItemList(sectionItem.children)



    }
  }



  override fun getItemCount() = categories.size

  class ParentViewHolder(val binding: ItemSectionBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onRowItemClick(isPerihsbale: Boolean, id: String, context: Context) {
    function.invoke(id,isPerihsbale)
  }
}
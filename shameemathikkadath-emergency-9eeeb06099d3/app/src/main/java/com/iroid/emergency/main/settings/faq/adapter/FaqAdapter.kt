package com.iroid.emergency.main.settings.faq.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.emergency.databinding.ItemFaqBinding
import com.iroid.emergency.modal.common.faq.Faq
import kotlinx.android.synthetic.main.item_faq.view.*

class FaqAdapter(
    private val context: Context,
    private val faqList: List<Faq>
) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(faq: Faq) {
            binding.tvQA.text=faq.question
            binding.tvAns.text=faq.answer
            if(faq.isExpand){
                binding.constraintSub.visibility= View.VISIBLE
            }else{
                binding.constraintSub.visibility= View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(faqList[position])
        holder.itemView.ivIcon.setOnClickListener {
            val expanded: Boolean = faqList[position].isExpand
            faqList[position].isExpand=!expanded
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}

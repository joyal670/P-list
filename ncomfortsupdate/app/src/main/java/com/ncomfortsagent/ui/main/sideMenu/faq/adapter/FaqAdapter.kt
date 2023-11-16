package com.ncomfortsagent.ui.main.sideMenu.faq.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemFaqBinding
import com.ncomfortsagent.model.faq.AgentFaqFaq


class FaqAdapter(private var requireContext: Context, private var faqList: ArrayList<AgentFaqFaq>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view =
                    ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = faqList[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ViewHolder) {

            var clicked = false

            holder.binding.expandable.spinnerDrawable = ContextCompat.getDrawable(requireContext, R.drawable.ic_plus)
            holder.binding.expandable.spinnerColor = Color.parseColor("#828282")

            holder.binding.expandable.parentLayout.findViewById<MaterialTextView>(R.id.tvHeading).text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(faqList[position].question, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(faqList[position].question)
            }

            holder.binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvSubHeading).text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(faqList[position].answer, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(faqList[position].answer)
            }

            holder.binding.expandable.parentLayout.setOnClickListener {
                if (!clicked) {
                    holder.binding.expandable.expand()
                    holder.binding.expandable.spinnerDrawable =
                        ContextCompat.getDrawable(requireContext, R.drawable.ic_close)
                    holder.binding.expandable.spinnerColor = Color.parseColor("#828282")
                    clicked = true
                } else {
                    holder.binding.expandable.collapse()
                    holder.binding.expandable.spinnerDrawable =
                        ContextCompat.getDrawable(requireContext, R.drawable.ic_plus)
                    holder.binding.expandable.spinnerColor = Color.parseColor("#828282")
                    clicked = false
                }
            }
        }
    }

    class ViewHolder(var binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


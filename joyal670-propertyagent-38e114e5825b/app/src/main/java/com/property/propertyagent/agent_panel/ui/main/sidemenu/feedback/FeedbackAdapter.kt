package com.property.propertyagent.agent_panel.ui.main.sidemenu.feedback

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_feedback.Feedback
import com.property.propertyagent.utils.CommonUtils
import kotlinx.android.synthetic.main.recycle_feedback_list_item.view.*
import render.animations.Fade
import render.animations.Render

class FeedbackAdapter(
    private val context: Context,
    private var feedbackList: List<Feedback>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_feedback_list_item, parent, false)
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
        return feedbackList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (feedbackList[position].id) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        if (holder is ViewHolder) {
            val render = context.let { Render(it) }
            render.setAnimation(Fade().InDown(holder.itemView.item_feedback_body))
            render.setDuration(1000)

            holder.itemView.item_feedback_email.text = feedbackList[position].email
            holder.itemView.item_feedback_heading.text = feedbackList[position].name
            holder.itemView.item_feedback_body.text = feedbackList[position].comments
            holder.itemView.tvProfileName.text = feedbackList[position].name

            holder.itemView.item_feedback_profile_layout_image.setOnClickListener {
                CommonUtils.openWhatsAppEnquiry(context, feedbackList[position].phone, "")
            }

            holder.itemView.item_feedback_plus_icon.setOnClickListener {
                render.start()
                holder.itemView.item_feedback_body.visibility = View.VISIBLE
                holder.itemView.item_feedback_profile_layout.visibility = View.VISIBLE
                holder.itemView.item_feedback_profile_layout_image.visibility = View.VISIBLE
                holder.itemView.item_feedback_plus_icon.visibility = View.GONE
                holder.itemView.item_feedback_hide_icon.visibility = View.VISIBLE
            }
            holder.itemView.item_feedback_hide_icon.setOnClickListener {
                holder.itemView.item_feedback_body.visibility = View.GONE
                holder.itemView.item_feedback_profile_layout.visibility = View.GONE
                holder.itemView.item_feedback_profile_layout_image.visibility = View.GONE
                holder.itemView.item_feedback_plus_icon.visibility = View.VISIBLE
                holder.itemView.item_feedback_hide_icon.visibility = View.GONE
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


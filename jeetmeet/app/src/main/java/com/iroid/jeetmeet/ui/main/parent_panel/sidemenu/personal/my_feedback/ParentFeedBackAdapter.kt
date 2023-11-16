package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_feedback

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleParentFeedbackListItemBinding
import com.iroid.jeetmeet.modal.parent.feedbacks.ParentFeedbacksData


class ParentFeedBackAdapter(
    private var feedbackList: ArrayList<ParentFeedbacksData>,
    private var editFeedback: (Int) -> Unit,
    private var deleteFeedback: (Int) -> Unit
) : RecyclerView.Adapter<ParentFeedBackAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleParentFeedbackListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleParentFeedbackListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvParentMore.paintFlags = holder.binding.tvParentMore.paintFlags
        holder.binding.tvParentDesc.maxLines = 2

        var click = true
        holder.binding.tvParentMore.setOnClickListener {
            if (click) {
                click = false
                holder.binding.tvParentDesc.expandTextView()
                holder.binding.tvParentMore.text = context!!.getString(R.string.less)
            } else {
                click = true
                holder.binding.tvParentDesc.collapseTextView()
                holder.binding.tvParentMore.text = context!!.getString(R.string.more)
            }

        }

        holder.binding.tvParentDesc.text = feedbackList[position].note
        holder.binding.tvDate.text = "Date : " + feedbackList[position].date

        holder.binding.editBtn.setOnClickListener {
            editFeedback.invoke(feedbackList[position].id)
        }

        holder.binding.deleteBtn.setOnClickListener {
            deleteFeedback.invoke(feedbackList[position].id)
        }

    }
}

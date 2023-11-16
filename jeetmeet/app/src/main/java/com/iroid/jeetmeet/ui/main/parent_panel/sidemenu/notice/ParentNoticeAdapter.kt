package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.notice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentDashboardListItemBinding
import com.iroid.jeetmeet.modal.parent.dashboard.ParentDashboardNotice


class ParentNoticeAdapter(
    private var noticeList: ArrayList<ParentDashboardNotice>,
    private var viewNotice: (Int) -> Unit
) : RecyclerView.Adapter<ParentNoticeAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentDashboardListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentDashboardListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvDashbordTitle.text = noticeList[position].title
        holder.binding.tvDashboardContent.text = noticeList[position].description

        /* for hiding view for last item */
        if (position == itemCount - 1) {
            holder.binding.sampleView.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            viewNotice.invoke(noticeList[position].id)
        }
    }
}

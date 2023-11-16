package com.iroid.jeetmeet.ui.main.student_panel.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentDashboardListItemBinding
import com.iroid.jeetmeet.modal.student.dashboard.StudentDashboardNotice


class StudentDashboardAdapter(
    private var noticeList: ArrayList<StudentDashboardNotice>,
    private var viewNotice: (Int) -> Unit
) : RecyclerView.Adapter<StudentDashboardAdapter.ViewHold>() {
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

package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.calender

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentAssignmentListItemBinding
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsAssignment


class StudentAssignmentAdapter(private var diariesList: ArrayList<StudentAssignmentsAssignment>) :
    RecyclerView.Adapter<StudentAssignmentAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentAssignmentListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentAssignmentListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return diariesList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvStudentAssignmentDesc.text = diariesList[position].name
        holder.binding.tvDate.text = diariesList[position].deadline_date.slice(8..9)
    }
}

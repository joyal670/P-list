package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.staff_directory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentStaffDirectoryListItemBinding
import com.iroid.jeetmeet.modal.student.staff_directory.StudentStaffDirectoryData


class StudentStaffDirectoryAdapter(private var staffDirectoryData: ArrayList<StudentStaffDirectoryData>) :
    RecyclerView.Adapter<StudentStaffDirectoryAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentStaffDirectoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentStaffDirectoryListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return staffDirectoryData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvSubjectName.text = staffDirectoryData[position].subject
        holder.binding.tvTeacherName.text = staffDirectoryData[position].teachers
        if (staffDirectoryData[position].teachers.isBlank()){
            holder.binding.tvTeacherName.text = "No Teachers available"
        }
    }
}

package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.online_class

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentOnlineClassListItemBinding
import com.iroid.jeetmeet.modal.student.online_class.StudentOnlineClassData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StudentOnlineClassAdapter(
    private var onlineClassList: ArrayList<StudentOnlineClassData>,
    private var startClass: (Int) -> Unit
) : RecyclerView.Adapter<StudentOnlineClassAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentOnlineClassListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentOnlineClassListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return onlineClassList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val outputFormat: DateFormat = SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate: Date? = inputFormat.parse(onlineClassList[position].date)
        val outputStartDate: String? = outputFormat.format(startDate)

        holder.binding.tvDate.text = "Date : $outputStartDate"

        val inputTimeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputTimeFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val inputTime1: Date? = inputTimeFormat.parse(onlineClassList[position].start_time.take(5))
        val outputTime1: String = outputTimeFormat.format(inputTime1)
        val inputTime2: Date? = inputTimeFormat.parse(onlineClassList[position].end_time.take(5))
        val outputTime2: String = outputTimeFormat.format(inputTime2)

        holder.binding.tvTime.text = "$outputTime1 - $outputTime2"

        holder.binding.tvExamTitle.text = onlineClassList[position].title
        holder.binding.tvClass.text =
            onlineClassList[position].classname.name + " " + onlineClassList[position].divisions.name
        holder.binding.tvSubject.text = onlineClassList[position].subjects.name

        holder.binding.startBtn.setOnClickListener {
            startClass.invoke(onlineClassList[position].id)
        }
    }
}

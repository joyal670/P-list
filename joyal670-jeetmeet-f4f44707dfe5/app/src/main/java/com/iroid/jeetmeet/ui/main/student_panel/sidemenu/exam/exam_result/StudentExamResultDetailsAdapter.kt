package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_result

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentExamResultDetailsListItemBinding
import com.iroid.jeetmeet.modal.student.exam_result_details.StudentExamResultDetailsData


class StudentExamResultDetailsAdapter(
    private var examDetailsList: ArrayList<StudentExamResultDetailsData>,
    private var ques: (Int, Int, String, String) -> Unit
) : RecyclerView.Adapter<StudentExamResultDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentExamResultDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentExamResultDetailsListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return examDetailsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvQuesNo.text = "# " + examDetailsList[position].question.toString()
        holder.binding.tvques.text = examDetailsList[position].questions.question
        holder.binding.tvQuesMark.text =
            "Question Mark - " + examDetailsList[position].total_mark.toString()
        holder.binding.tvQuesMarkResult.text = "Mark - " + examDetailsList[position].mark.toString()

        holder.binding.questBtn.setOnClickListener {
            ques.invoke(
                examDetailsList[position].total_mark,
                examDetailsList[position].mark,
                examDetailsList[position].questions.question,
                examDetailsList[position].answer
            )
        }
    }
}

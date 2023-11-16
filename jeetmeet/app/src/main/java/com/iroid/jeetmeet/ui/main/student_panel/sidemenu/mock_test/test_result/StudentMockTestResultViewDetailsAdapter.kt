package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_result

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentMockTestResultViewDetailsListItemBinding
import com.iroid.jeetmeet.modal.student.mock_test_result_view.StudentMockTestResultViewResult


class StudentMockTestResultViewDetailsAdapter(
    private var examDetailsList: ArrayList<StudentMockTestResultViewResult>,
    private var ques: (Int) -> Unit
) : RecyclerView.Adapter<StudentMockTestResultViewDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleStudentMockTestResultViewDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentMockTestResultViewDetailsListItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return examDetailsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvQuesNo.text = "# " + (position+1)
        holder.binding.tvques.text = examDetailsList[position].question
        holder.binding.tvQuesMark.text =
            "Total Options - " + examDetailsList[position].totaloption.toString()
        holder.binding.tvQuesMarkResult.text = "Mark - " + examDetailsList[position].mark.toString()


        if (examDetailsList[position].is_correct == 1) {
            holder.binding.tvQuesIsCorrect.text = "Yes"
            holder.binding.tvQuesIsCorrect.setTextColor(Color.parseColor("#6AC58C"))
        } else {
            holder.binding.tvQuesIsCorrect.text = " No"
            holder.binding.tvQuesIsCorrect.setTextColor(Color.parseColor("#EE2424"))
        }


        holder.binding.questBtn.setOnClickListener {
            ques.invoke(examDetailsList[position].id)
        }
    }
}

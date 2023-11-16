package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecyclerviewQuestionListItemBinding
import com.iroid.jeetmeet.modal.student.exam_start.StudentExamStartOption
import com.iroid.jeetmeet.modal.student.exam_start.StudentExamStartQuestion


class StudentExamQuestionsAdapter(
    private var questionList: ArrayList<StudentExamStartQuestion>,
    private var singleAns: (Int, String) -> Unit,
    private var descriptiveAns: (Int, String) -> Unit,
    private var objectiveAns: (Int, Int) -> Unit
) : RecyclerView.Adapter<StudentExamQuestionsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecyclerviewQuestionListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecyclerviewQuestionListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.sm.text = questionList[position].types.name
        holder.binding.materialTextView5.text = questionList[position].question



        when (questionList[position].types.id) {
            /* Single Answer */
            1 -> {
                holder.binding.sm.text = questionList[position].types.name
                holder.binding.editText2.visibility = View.VISIBLE
                holder.binding.ll.visibility = View.GONE

                holder.binding.editText2.doOnTextChanged { text, start, before, count ->
                    singleAns.invoke(questionList[position].id, text.toString())
                }
            }

            /* Objective */
            2 -> {
                holder.binding.sm.text = questionList[position].types.name
                holder.binding.editText2.visibility = View.GONE
                holder.binding.ll.visibility = View.VISIBLE

                val opsize = questionList[position].option.size
                val optionList = ArrayList<StudentExamStartOption>()
                optionList.addAll(questionList[position].option)


                optionList.forEach {
                    val radioButton1 = RadioButton(context)
                    radioButton1.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    radioButton1.text = it.name
                    radioButton1.id = it.id
                    holder.binding.radioGroup.addView(radioButton1)
                }
                holder.binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    objectiveAns.invoke(questionList[position].id, checkedId)
                }
            }

            /* Discript */
            3 -> {
                holder.binding.sm.text = questionList[position].types.name
                holder.binding.editText2.visibility = View.VISIBLE
                holder.binding.ll.visibility = View.GONE

                holder.binding.editText2.doOnTextChanged { text, start, before, count ->
                    descriptiveAns.invoke(questionList[position].id, text.toString())
                }
            }

        }
    }

}

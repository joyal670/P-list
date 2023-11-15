package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_result

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentExamResultListItemBinding
import com.iroid.jeetmeet.modal.student.exam_result.StudentExamResultExamResult
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentExamResultAdapter(
    private var examResultList: ArrayList<StudentExamResultExamResult>,
    private var viewResult: (Int) -> Unit
) : RecyclerView.Adapter<StudentExamResultAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentExamResultExamResult>()

    init {
        copyList = examResultList
    }

    class ViewHold(var binding: RecycleStudentExamResultListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentExamResultListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val outputFormat: DateFormat = SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate: Date? = inputFormat.parse(copyList[position].edate)
        val outputStartDate: String? = outputFormat.format(startDate)

        holder.binding.tvDate.text = "Date : $outputStartDate"

        val inputTimeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputTimeFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val inputTime1: Date? = inputTimeFormat.parse(copyList[position].examfrom.take(5))
        val outputTime1: String = outputTimeFormat.format(inputTime1)
        val inputTime2: Date? = inputTimeFormat.parse(copyList[position].examto.take(5))
        val outputTime2: String = outputTimeFormat.format(inputTime2)

        holder.binding.tvTime.text = "$outputTime1 - $outputTime2"

        holder.binding.tvTest.text = copyList[position].name
        holder.binding.tvExamCategory.text = copyList[position].exams_category.name
        holder.binding.tvClass.text =
            copyList[position].classes.name + " " + copyList[position].divisions.name
        holder.binding.tvSubject.text = copyList[position].subjects.name
        holder.binding.tvRoom.text = copyList[position].rooms.name
        holder.binding.tvInstructions.text = copyList[position].instructions.title

        holder.binding.viewBtn.setOnClickListener {
            viewResult.invoke(copyList[position].id)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = examResultList
                } else {
                    val resultList = ArrayList<StudentExamResultExamResult>()
                    for (row in examResultList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    copyList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = copyList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                copyList = results?.values as ArrayList<StudentExamResultExamResult>
                notifyDataSetChanged()
            }

        }
    }
}

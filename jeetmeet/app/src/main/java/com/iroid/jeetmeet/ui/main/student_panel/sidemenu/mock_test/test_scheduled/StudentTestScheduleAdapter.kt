package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentTestScheduleListItemBinding
import com.iroid.jeetmeet.modal.student.mock_test_list.StudentMockTestListTest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList


class StudentTestScheduleAdapter(
    private var mockList: ArrayList<StudentMockTestListTest>,
    private var testId: (Int) -> Unit
) : RecyclerView.Adapter<StudentTestScheduleAdapter.ViewHold>(), Filterable {

    private var context: Context? = null
    private var copyList = ArrayList<StudentMockTestListTest>()

    init {
        copyList = mockList
    }

    class ViewHold(var binding: RecycleStudentTestScheduleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentTestScheduleListItemBinding.inflate(
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

        /* for checking test date and time */
        val sdf1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdf2: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = sdf1.format(Date())
        val currentTime = sdf2.format(Date())

        val target: LocalTime = LocalTime.parse(currentTime)
        if (copyList[position].tdate == currentDate) {
            if (copyList[position].attend.isEmpty()) {
                if (target.isAfter(LocalTime.parse(copyList[position].testfrom.take(5)))) {

                    if (target.isBefore(LocalTime.parse(copyList[position].testto.take(5)))) {
                        holder.binding.attentExamBtn.isEnabled = true
                        holder.binding.attentExamBtn.isClickable = true
                        holder.binding.attentExamBtn.isActivated = true
                        holder.binding.attentExamBtn.backgroundTintList =
                            ContextCompat.getColorStateList(context!!, R.color.comet)
                    }

                }
            }
        } else {
            holder.binding.attentExamBtn.isEnabled = false
            holder.binding.attentExamBtn.isClickable = false
            holder.binding.attentExamBtn.isActivated = false
            holder.binding.attentExamBtn.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.comet_light)
        }

        holder.binding.attentExamBtn.setOnClickListener {
            testId.invoke(copyList[position].id)
        }


        val outputFormat: DateFormat = SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate: Date? = inputFormat.parse(copyList[position].tdate)
        val outputStartDate: String? = outputFormat.format(startDate)

        holder.binding.tvDate.text = "Date : $outputStartDate"

        val inputTimeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputTimeFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val inputTime1: Date? = inputTimeFormat.parse(copyList[position].testfrom.take(5))
        val outputTime1: String = outputTimeFormat.format(inputTime1)
        val inputTime2: Date? = inputTimeFormat.parse(copyList[position].testto.take(5))
        val outputTime2: String = outputTimeFormat.format(inputTime2)

        holder.binding.tvTime.text = "$outputTime1 - $outputTime2"

        holder.binding.tvExamTitle.text = copyList[position].name
        holder.binding.tvClass.text =
            copyList[position].classname.name + " " + copyList[position].divisionname.name
        holder.binding.tvSubject.text = copyList[position].subjectname.name
        holder.binding.tvDes.text = copyList[position].instructionname.title

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = mockList
                } else {
                    val resultList = ArrayList<StudentMockTestListTest>()
                    for (row in mockList) {
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
                copyList = results?.values as ArrayList<StudentMockTestListTest>
                notifyDataSetChanged()
            }

        }
    }

}

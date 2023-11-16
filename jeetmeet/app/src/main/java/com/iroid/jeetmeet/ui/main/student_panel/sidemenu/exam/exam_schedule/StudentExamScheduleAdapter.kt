package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentExamScheduleListItemBinding
import com.iroid.jeetmeet.modal.student.exam_schedule.StudentExamScheduleData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class StudentExamScheduleAdapter(
    private var examScheduleList: ArrayList<StudentExamScheduleData>,
    private var examID: (Int) -> Unit
) : RecyclerView.Adapter<StudentExamScheduleAdapter.ViewHold>(), Filterable {

    private var context: Context? = null
    private var copyList = ArrayList<StudentExamScheduleData>()

    init {
        copyList = examScheduleList
    }


    class ViewHold(var binding: RecycleStudentExamScheduleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentExamScheduleListItemBinding.inflate(
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

        holder.binding.tvExamTitle.text = copyList[position].name
        holder.binding.tvExamCategory.text = ": " + copyList[position].exams_category.name
        holder.binding.tvClass.text =
            ": " + copyList[position].classes.name + " " + copyList[position].divisions.name
        holder.binding.tvSubject.text = ": " + copyList[position].subjects.name
        holder.binding.tvRoom.text = copyList[position].rooms.name
        holder.binding.tvDes.text = copyList[position].instructions.title


        /* for checking exam date and time */
        val sdf1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdf2: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = sdf1.format(Date())
        val currentTime = sdf2.format(Date())

        val target: LocalTime = LocalTime.parse(currentTime)

        if (copyList[position].edate == currentDate) {
            if (copyList[position].attant.isEmpty()) {
                if (target.isAfter(LocalTime.parse(copyList[position].examfrom.take(5)))) {

                    if (target.isBefore(LocalTime.parse(copyList[position].examto.take(5)))) {
                        holder.binding.attentExamButton.isEnabled = true
                        holder.binding.attentExamButton.isClickable = true
                        holder.binding.attentExamButton.isActivated = true
                        holder.binding.attentExamButton.backgroundTintList =
                            ContextCompat.getColorStateList(context!!, R.color.comet)
                    }

                }
            }
        } else {
            holder.binding.attentExamButton.isEnabled = false
            holder.binding.attentExamButton.isClickable = false
            holder.binding.attentExamButton.isActivated = false
            holder.binding.attentExamButton.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.comet_light)
        }

        holder.binding.attentExamButton.setOnClickListener {
            examID.invoke(copyList[position].id)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = examScheduleList
                } else {
                    val resultList = ArrayList<StudentExamScheduleData>()
                    for (row in examScheduleList) {
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
                copyList = results?.values as ArrayList<StudentExamScheduleData>
                notifyDataSetChanged()
            }

        }
    }
}

package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.leave_apply

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentLeaveApplyListItemBinding
import com.iroid.jeetmeet.dialogs.FullScreenImageDialogFragment
import com.iroid.jeetmeet.modal.student.leave_apply.StudentLeaveApplyData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StudentLeaveApplyAdapter(
    private var leaveList: ArrayList<StudentLeaveApplyData>,
    private var edit: (Int) -> Unit,
    private var delete: (Int) -> Unit
) : RecyclerView.Adapter<StudentLeaveApplyAdapter.ViewHold>(),
    Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentLeaveApplyData>()

    init {
        copyList = leaveList
    }

    class ViewHold(var binding: RecycleStudentLeaveApplyListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentLeaveApplyListItemBinding.inflate(
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
        holder.binding.tvLeaveDate.text = "Date : " + copyList[position].apply_date.take(10)
        when (copyList[position].status) {
            0 -> {
                holder.binding.statusBtn.text = "Pending"
                holder.binding.statusBtn.backgroundTintList =
                    ContextCompat.getColorStateList(context!!, R.color.flush_orange)
            }

            1 -> {
                holder.binding.statusBtn.text = "Approved"
                holder.binding.statusBtn.backgroundTintList =
                    ContextCompat.getColorStateList(context!!, R.color.forest_green)
            }

            2 -> {
                holder.binding.statusBtn.text = "Rejected"
                holder.binding.statusBtn.backgroundTintList =
                    ContextCompat.getColorStateList(context!!, R.color.pomegranate)
            }
        }

        holder.binding.tvStudentName.text =
            copyList[position].studentname.first_name + " " + copyList[position].studentname.middle_name + " " + copyList[position].studentname.last_name
        holder.binding.tvCatagoryName.text = copyList[position].leave_categoryname.name

        val outputFormat: DateFormat = SimpleDateFormat("EE-MMM-yyyy", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val startDate: Date? = inputFormat.parse(copyList[position].from_date)
        val endDate: Date? = inputFormat.parse(copyList[position].to_date)
        val outputStartDate: String? = outputFormat.format(startDate)
        val outputEndDate: String? = outputFormat.format(endDate)

        holder.binding.tvDate.text = "$outputStartDate  -  $outputEndDate"
        holder.binding.tvNoOfDays.text = copyList[position].leave_days.toString()
        holder.binding.tvReason.text = copyList[position].reason
        holder.binding.attachmentBtn.text = copyList[position].attachment

        holder.binding.attachmentBtn.setOnClickListener {
            if (copyList[position].attachment_url != null) {
                val activity = context as FragmentActivity
                val fm: FragmentManager = activity.supportFragmentManager
                val dialog = FullScreenImageDialogFragment(
                    copyList[position].attachment_url,
                    copyList[position].studentname.first_name
                )
                dialog.show(fm, "TAG")
            }
        }

        holder.binding.editBtn.setOnClickListener {
            edit.invoke(copyList[position].id)
        }

        holder.binding.deleteBtn.setOnClickListener {
            delete.invoke(copyList[position].id)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = leaveList
                } else {
                    val resultList = ArrayList<StudentLeaveApplyData>()
                    for (row in leaveList) {
                        if (row.reason.lowercase(Locale.ROOT)
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
                copyList = results?.values as ArrayList<StudentLeaveApplyData>
                notifyDataSetChanged()
            }

        }
    }
}

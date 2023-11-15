package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.assigned_leave

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentAssignedLeaveListItemBinding
import com.iroid.jeetmeet.modal.student.assigned_leave.StudentAssignedLeaveData
import java.util.*
import kotlin.collections.ArrayList


class StudentAssignedLeaveAdapter(private var leaveList: ArrayList<StudentAssignedLeaveData>) :
    RecyclerView.Adapter<StudentAssignedLeaveAdapter.ViewHold>(),
    Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentAssignedLeaveData>()

    init {
        copyList = leaveList
    }


    class ViewHold(var binding: RecycleStudentAssignedLeaveListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentAssignedLeaveListItemBinding.inflate(
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
        holder.binding.tvLeaveTitile.text = copyList[position].leavecategoryname.name
        holder.binding.tvNoOfDays.text = copyList[position].no_of_day.toString() + " Days"
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = leaveList
                } else {
                    val resultList = ArrayList<StudentAssignedLeaveData>()
                    for (row in leaveList) {
                        if (row.leavecategoryname.name.lowercase(Locale.ROOT)
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
                copyList = results?.values as ArrayList<StudentAssignedLeaveData>
                notifyDataSetChanged()
            }

        }
    }
}

package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.assigned_leave

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleParentLeaveListItemBinding
import com.iroid.jeetmeet.modal.parent.leave_assigned.ParentLeaveAssignedData
import java.util.*
import kotlin.collections.ArrayList


class ParentLeaveAdapter(private var leaveList: ArrayList<ParentLeaveAssignedData>) :
    RecyclerView.Adapter<ParentLeaveAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<ParentLeaveAssignedData>()

    init {
        copyList = leaveList
    }

    class ViewHold(var binding: RecycleParentLeaveListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleParentLeaveListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvStudentName.text = copyList[position].student_details.first_name
        holder.binding.tvNoOfDays.text = copyList[position].leaveassign.toString()
        holder.binding.tvCatagoryName.text = copyList[position].leavecategoryname.name
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = leaveList
                } else {
                    val resultList = ArrayList<ParentLeaveAssignedData>()
                    for (row in leaveList) {
                        if (row.student_details.first_name.lowercase(Locale.ROOT)
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
                copyList = results?.values as ArrayList<ParentLeaveAssignedData>
                notifyDataSetChanged()
            }

        }
    }
}

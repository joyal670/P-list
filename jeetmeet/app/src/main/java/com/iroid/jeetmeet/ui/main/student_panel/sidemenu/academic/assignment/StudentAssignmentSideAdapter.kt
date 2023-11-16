package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.assignment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentAssignmentSideListItemBinding
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsAssignment
import java.util.*
import kotlin.collections.ArrayList


class StudentAssignmentSideAdapter(
    private var assignmentsList: ArrayList<StudentAssignmentsAssignment>,
    private var openBrowser: (String) -> Unit
) : RecyclerView.Adapter<StudentAssignmentSideAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentAssignmentsAssignment>()

    init {
        copyList = assignmentsList
    }

    class ViewHold(var binding: RecycleStudentAssignmentSideListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentAssignmentSideListItemBinding.inflate(
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
        holder.binding.tvStudentMore.paintFlags =
            holder.binding.tvStudentMore.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.binding.tvStudentDesc.maxLines = 2


        holder.binding.tvClass.text =
            "Class : " + copyList[position].classname.name + " " + copyList[position].divisionname.name
        holder.binding.tvAcadamicYear.text =
            "Academic Year : " + copyList[position].academic_yearname.name
        holder.binding.tvCatagoryName.text = copyList[position].name
        holder.binding.tvDeadLine.text = copyList[position].deadline_date.take(10)
        holder.binding.tvSubject.text = " :" + copyList[position].subjectname.name
        holder.binding.tvStudentDesc.text = copyList[position].description

        if (copyList[position].file == "") {
            holder.binding.attachmentBtn.visibility = View.GONE
        }
        holder.binding.attachmentBtn.text = copyList[position].file
        if(copyList[position].attachment_url.isNullOrBlank())
        {
            holder.binding.attachmentBtn.text = "No Files"
        }
        holder.binding.attachmentBtn.setOnClickListener {
            if(copyList[position].attachment_url.isNullOrBlank())
            {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Not found")
                    .setMessage("unable to find attachment")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
                    }.show()
            }
            else{
                openBrowser.invoke(copyList[position].attachment_url)
            }

        }


        var click = true
        holder.binding.tvStudentMore.setOnClickListener {
            if (click) {
                click = false
                holder.binding.tvStudentDesc.expandTextView()
                holder.binding.tvStudentMore.text = context!!.getString(R.string.less)
            } else {
                click = true
                holder.binding.tvStudentDesc.collapseTextView()
                holder.binding.tvStudentMore.text = context!!.getString(R.string.more)
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = assignmentsList
                } else {
                    val resultList = ArrayList<StudentAssignmentsAssignment>()
                    for (row in assignmentsList) {
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
                copyList = results?.values as ArrayList<StudentAssignmentsAssignment>
                notifyDataSetChanged()
            }

        }
    }
}

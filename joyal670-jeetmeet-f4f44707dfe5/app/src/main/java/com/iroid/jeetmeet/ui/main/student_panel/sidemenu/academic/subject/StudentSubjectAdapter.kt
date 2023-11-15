package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.subject

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentSubjectListItemBinding
import com.iroid.jeetmeet.modal.student.subjects.StudentSubjectSubject
import java.util.*
import kotlin.collections.ArrayList


class StudentSubjectAdapter(
    private var subjectsList: ArrayList<StudentSubjectSubject>,
    private var subjectsClass: String,
    private var subjectsDivision: String,
) : RecyclerView.Adapter<StudentSubjectAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentSubjectSubject>()

    init {
        copyList = subjectsList
    }

    class ViewHold(var binding: RecycleStudentSubjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentSubjectListItemBinding.inflate(
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
        holder.binding.tvClass.text = "Class : $subjectsClass $subjectsDivision"
        holder.binding.tvSubjectCode.text = "Subject Code : " + copyList[position].subject_code
        holder.binding.tvSubjectName.text = copyList[position].name
        holder.binding.tvTeacherName.text = copyList[position].teachers
        holder.binding.tvSubjectAuthor.text = copyList[position].author
        holder.binding.tvPassMark.text = copyList[position].pass_mark.toString()
        holder.binding.tvFinalMark.text = copyList[position].total_mark.toString()
        holder.binding.tvNote.text = copyList[position].note

        if(copyList[position].teachers.isBlank()){
            holder.binding.tvTeacherName.text = "No Teachers available"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = subjectsList
                } else {
                    val resultList = ArrayList<StudentSubjectSubject>()
                    for (row in subjectsList) {
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
                copyList = results?.values as ArrayList<StudentSubjectSubject>
                notifyDataSetChanged()
            }

        }
    }
}

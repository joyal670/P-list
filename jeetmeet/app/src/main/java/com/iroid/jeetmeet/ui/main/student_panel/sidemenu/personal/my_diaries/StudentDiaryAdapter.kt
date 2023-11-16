package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.my_diaries

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentDiaryListItemBinding
import com.iroid.jeetmeet.modal.student.diaries.StudentDiariesData
import java.util.*
import kotlin.collections.ArrayList


class StudentDiaryAdapter(
    private var diariesList: ArrayList<StudentDiariesData>,
    private var deleteDiaries: (Int) -> Unit,
    private var updateDiaries: (Int) -> Unit
) : RecyclerView.Adapter<StudentDiaryAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentDiariesData>()

    init {
        copyList = diariesList
    }

    class ViewHold(var binding: RecycleStudentDiaryListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleStudentDiaryListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvStudentNumber.text = copyList[position].id.toString()
        holder.binding.tvStudentTitle.text = copyList[position].note
        holder.binding.tvStudentDate.text = "Date : " + copyList[position].date

        holder.binding.deleteBtn.setOnClickListener {
            deleteDiaries.invoke(copyList[position].id)
        }

        holder.binding.updateBtn.setOnClickListener {
            updateDiaries.invoke(copyList[position].id)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = diariesList
                } else {
                    val resultList = ArrayList<StudentDiariesData>()
                    for (row in diariesList) {
                        if (row.note.lowercase(Locale.ROOT)
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
                copyList = results?.values as ArrayList<StudentDiariesData>
                notifyDataSetChanged()
            }

        }
    }
}

package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.events

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentEventListItemBinding
import com.iroid.jeetmeet.modal.student.events.StudentEventsData
import java.util.*
import kotlin.collections.ArrayList

class StudentEventsAdapter(private var eventsList: ArrayList<StudentEventsData>) :
    RecyclerView.Adapter<StudentEventsAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentEventsData>()

    init {
        copyList = eventsList
    }

    class ViewHold(var binding: RecycleStudentEventListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleStudentEventListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.tvEventName.text = copyList[position].title
        holder.binding.tvEventType.text = ": " + copyList[position].type
        holder.binding.tvEventDate.text = ": " + copyList[position].start_date
        holder.binding.tvEventExpDate.text = ": " + copyList[position].exp_date
        holder.binding.tvEventClass.text =
            ": " + copyList[position].`class` + " " + copyList[position].division
        holder.binding.tvEventTime.text = ": " + copyList[position].time

        if(copyList[position].`class`.isBlank()){
            holder.binding.tvEventClass.text = "Common For All"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = eventsList
                } else {
                    val resultList = ArrayList<StudentEventsData>()
                    for (row in eventsList) {
                        if (row.title.lowercase(Locale.ROOT)
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
                copyList = results?.values as ArrayList<StudentEventsData>
                notifyDataSetChanged()
            }

        }
    }
}

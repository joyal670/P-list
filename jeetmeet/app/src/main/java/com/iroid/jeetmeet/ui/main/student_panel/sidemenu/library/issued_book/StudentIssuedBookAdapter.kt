package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.library.issued_book

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleStudentIssuedBookListItemBinding
import com.iroid.jeetmeet.modal.student.issued_books.StudentIssuedBookData
import java.util.*
import kotlin.collections.ArrayList


class StudentIssuedBookAdapter(
    private var issuedBookList: ArrayList<StudentIssuedBookData>,
    private var openBrowser: (String) -> Unit
) : RecyclerView.Adapter<StudentIssuedBookAdapter.ViewHold>(), Filterable {

    private var context: Context? = null
    private var copyList = ArrayList<StudentIssuedBookData>()

    init {
        copyList = issuedBookList
    }

    class ViewHold(var binding: RecycleStudentIssuedBookListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentIssuedBookListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.binding.reqBookStatus.text = copyList[position].status
        holder.binding.tvBookName.text = copyList[position].name
        holder.binding.tvBookCategory.text = copyList[position].category
        holder.binding.tvBookAuthor.text = copyList[position].author
        holder.binding.tvBookPrice.text = copyList[position].price.toString()
        holder.binding.tvBookRackNo.text = copyList[position].rack.toString()

        holder.binding.ivBook.setOnClickListener {
            openBrowser.invoke(copyList[position].pdf)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = issuedBookList
                } else {
                    val resultList = ArrayList<StudentIssuedBookData>()
                    for (row in issuedBookList) {
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
                copyList = results?.values as ArrayList<StudentIssuedBookData>
                notifyDataSetChanged()
            }

        }
    }
}

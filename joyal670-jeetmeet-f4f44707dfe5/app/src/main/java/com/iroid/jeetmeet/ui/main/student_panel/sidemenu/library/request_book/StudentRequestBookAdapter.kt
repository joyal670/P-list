package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.library.request_book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleStudentRequestBookListItemBinding
import com.iroid.jeetmeet.modal.student.request_book.StudentRequestBookData
import java.util.*
import kotlin.collections.ArrayList


class StudentRequestBookAdapter(
    private var bookList: ArrayList<StudentRequestBookData>,
    private var openBrowser: (String) -> Unit,
    private var requestBook: (Int) -> Unit,
    private var cancelBook: (Int) -> Unit
) : RecyclerView.Adapter<StudentRequestBookAdapter.ViewHold>(), Filterable {
    private var context: Context? = null
    private var copyList = ArrayList<StudentRequestBookData>()

    init {
        copyList = bookList
    }

    class ViewHold(var binding: RecycleStudentRequestBookListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleStudentRequestBookListItemBinding.inflate(
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
        holder.binding.tvSubject.text = copyList[position].subject
        holder.binding.tvBookRackNo.text = copyList[position].rack.toString()


        var click = true
        holder.binding.tvMoreDetails.setOnClickListener {
            if (click) {
                holder.binding.sampleLayout.visibility = View.VISIBLE
                holder.binding.tvMoreDetails.text = context!!.getString(R.string.less_details)
                holder.binding.tvMoreDetails.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_grey,
                    0
                )
                click = false
            } else {
                holder.binding.sampleLayout.visibility = View.GONE
                holder.binding.tvMoreDetails.text = context!!.getString(R.string.more_details)
                holder.binding.tvMoreDetails.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_grey,
                    0
                )
                click = true
            }

        }

        /* request book */
        if (!copyList[position].requested) {
            //holder.binding.reqBookStatus.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.killarney)
            holder.binding.reqBookRequestBtn.text = context!!.getString(R.string.request)
            holder.binding.reqBookStatus.setBackgroundResource(R.drawable.drawable_available_box)
        }

        /* cancel  book */
        if (copyList[position].requested) {
            holder.binding.reqBookRequestBtn.text = context!!.getString(R.string.cancel)
            //holder.binding.reqBookStatus.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.flush_orange)
            holder.binding.reqBookRequestBtn.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.pomegranate)
            holder.binding.reqBookStatus.setBackgroundResource(R.drawable.drawable_requested_box)
        }

        /* download book */
        holder.binding.ivBook.setOnClickListener {
            openBrowser.invoke(copyList[position].pdf)
        }

        /* request or cancel book */
        holder.binding.reqBookRequestBtn.setOnClickListener {

            /* True -> book requested, so cancel book
            *  False -> book not requested, so request book */

            /* Cancel book by request_id
            *  request book by book_id */
            if (copyList[position].requested) {
                cancelBook.invoke(copyList[position].request_id)
            } else if (!copyList[position].requested) {
                requestBook.invoke(copyList[position].id)
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = bookList
                } else {
                    val resultList = ArrayList<StudentRequestBookData>()
                    for (row in bookList) {
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
                copyList = results?.values as ArrayList<StudentRequestBookData>
                notifyDataSetChanged()
            }

        }
    }
}

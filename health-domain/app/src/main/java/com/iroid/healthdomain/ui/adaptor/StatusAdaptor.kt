package com.iroid.healthdomain.ui.adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.anychart.math.cci.Context
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.databinding.ListItemStatusBinding
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class StatusAdaptor(val listener: AdaptorInterface, private val context: FragmentActivity) : RecyclerView.Adapter<StatusAdaptor.MyViewHolder>() ,
    Filterable {

    private var copyList = ArrayList<ContactData>()

    var list: List<ContactData> = arrayListOf()
        set(value) {
            field = value
            copyList.clear()

            list.forEach {
                if(it.name!=null){
                    copyList.add(it)
                }
            }

            copyList.sortWith(Comparator { p0, p1 ->
                p0!!.name.lowercase(Locale.ROOT)compareTo(p1!!.name.lowercase(Locale.ROOT)) })
            notifyDataSetChanged()
        }

    inner class MyViewHolder(var binding: ListItemStatusBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun setModel(statusModel: ContactData) {
            binding.contact = statusModel
            binding.card.setOnClickListener {
                listener.onItemClick(statusModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
                ListItemStatusBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setModel(copyList[position])
        when {
            copyList[position].hds.toDouble().roundToInt()<=25 -> {
                holder.binding.materialTextView11.text="Bad"
                holder.binding.materialTextView11.visibility= View.VISIBLE
                holder.binding.materialTextView12.visibility= View.GONE
            }
            copyList[position].hds.toDouble().roundToInt()<=50 -> {
                holder.binding.materialTextView11.text="Poor"
                holder.binding.materialTextView11.visibility= View.VISIBLE
                holder.binding.materialTextView12.visibility= View.GONE

            }
            copyList[position].hds.toDouble().roundToInt()<=75 -> {
                holder.binding.materialTextView11.visibility= View.GONE
                holder.binding.materialTextView12.visibility= View.VISIBLE
            }
            else -> {
                holder.binding.materialTextView11.visibility= View.GONE
                holder.binding.materialTextView12.visibility= View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = copyList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    copyList.clear()
                    list.forEach {
                        if(it.name!=null){
                            copyList.add(it)
                        }
                        copyList.sortWith(Comparator { p0, p1 ->
                            p0!!.name.lowercase(Locale.ROOT)compareTo(p1!!.name.lowercase(Locale.ROOT)) })
                    }
                } else {
                    val resultList = ArrayList<ContactData>()
                    for (row in list) {
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

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                copyList = results?.values as ArrayList<ContactData>
                notifyDataSetChanged()
            }
        }
    }
}

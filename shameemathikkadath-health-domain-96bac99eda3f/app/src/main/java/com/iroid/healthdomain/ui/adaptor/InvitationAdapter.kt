package com.iroid.healthdomain.ui.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

import com.iroid.healthdomain.data.dummyModel.NewContactsModel
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData


import com.iroid.healthdomain.databinding.ListItemInviteBinding
import com.iroid.healthdomain.ui.adptorInterface.InviteInterface
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class InvitationAdapter(val listener: InviteInterface): RecyclerView.Adapter<InvitationAdapter.MyViewHolder>(),
    Filterable {

    private var copyList = ArrayList<NewContactsModel>()

    var list: List<NewContactsModel> = arrayListOf()
        set(value) {
            field = value
            copyList = list as ArrayList<NewContactsModel>
            copyList.sortWith(Comparator { p0, p1 -> p0!!.name.compareTo(p1!!.name) })
            notifyDataSetChanged()
        }

    init {
        copyList = list as ArrayList<NewContactsModel>
    }

    inner class MyViewHolder(var binding: ListItemInviteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setModel(contactModel: NewContactsModel) {
            binding.contact = contactModel
            binding.cardItem.setOnClickListener {
                listener.onInviteItemClick(contactModel.name)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemInviteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setModel(copyList[position])
    }

    override fun getItemCount(): Int {
     return copyList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = list as ArrayList<NewContactsModel>
                } else {
                    val resultList = ArrayList<NewContactsModel>()
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
                copyList = results?.values as ArrayList<NewContactsModel>
                listener.onRefresh()
                notifyDataSetChanged()

            }
        }
    }
}
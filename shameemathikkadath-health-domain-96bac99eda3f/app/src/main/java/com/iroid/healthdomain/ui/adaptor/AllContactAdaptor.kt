package com.iroid.healthdomain.ui.adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.databinding.ListItemContactBinding
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.preference.ConstantPreference
import com.iroid.healthdomain.ui.utils.CommonUtils
import com.iroid.healthdomain.ui.utils.userPreferences
import java.util.*
import kotlin.collections.ArrayList

class AllContactAdaptor(val listener: AdaptorInterface ) : RecyclerView.Adapter<AllContactAdaptor.MyViewHolder>() ,
    Filterable {
    private var copyList = ArrayList<ContactData>()
    private var copyNewList = ArrayList<ContactData>()

    var list: List<ContactData> = arrayListOf()
        set(value) {
            copyList.clear()
            field = value
            list.forEach {
                if(it.name!=null){
                    copyList.add(it)
                }
            }
            copyList.sortWith(Comparator { p0, p1 ->
                p0!!.name.lowercase(Locale.ROOT)compareTo(p1!!.name.lowercase(Locale.ROOT)) })
            notifyDataSetChanged()

        }


    init {
        copyList = list as ArrayList<ContactData>
        copyNewList = copyList
    }


    inner class MyViewHolder(var binding: ListItemContactBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun setModel(contactModel: ContactData) {
            binding.contact = contactModel



            val followersList: ArrayList<String> = AppPreferences.getArray(ConstantPreference.NEW_FOLLOWER)!!
            Log.e("123456", "setModel: $followersList" )
            if(followersList.contains(contactModel.id.toString())){
                binding.imageView6.setBackgroundResource(R.drawable.ic_heart_filled)
            }else{
                binding.imageView6.setBackgroundResource(R.drawable.ic_heart_outlined)
            }

            binding.cardItem.setOnClickListener {
                listener.onItemClick(contactModel)
            }
            binding.imageView6.setOnClickListener {
                listener.favorite(contactModel.id,contactModel.is_following)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
                ListItemContactBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setModel(copyNewList[position])

    }

    override fun getItemCount(): Int = copyNewList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                copyNewList = if (charSearch.isEmpty()) {
                    copyList

                }else {
                    val resultList = ArrayList<ContactData>()
                    for (row in copyList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = copyNewList
                return filterResults

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                copyList = results?.values as ArrayList<ContactData>
//                notifyDataSetChanged()
                copyNewList = results?.values as ArrayList<ContactData>
                listener.refreshAllContact()
                notifyDataSetChanged()


            }
        }

    }
}

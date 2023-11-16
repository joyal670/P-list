package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_page

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_maintance.Service
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_owner_service_adapter_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class ServiceAdapter(
    private var maintanaceList : ArrayList<Service> ,
    private var navigateActivity : (Int , String) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ViewHold>() , Filterable {
    private var context : Context? = null
    private var copyList = ArrayList<Service>()

    init {
        copyList = maintanaceList
    }


    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_owner_service_adapter_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return copyList.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        holder.itemView.tvServiceName.text = copyList[position].service
        holder.itemView.sivServiceImage.loadImagesWithGlideExt(copyList[position].image)
        holder.itemView.btnChoose.setOnClickListener {
            navigateActivity.invoke(copyList[position].id , copyList[position].service)
        }

    }

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(constraint : CharSequence?) : FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = maintanaceList
                } else {
                    val resultList = ArrayList<Service>()
                    for (row in maintanaceList) {
                        if (row.service.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
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
            override fun publishResults(constraint : CharSequence? , results : FilterResults?) {
                copyList = results?.values as ArrayList<Service>
                notifyDataSetChanged()
            }

        }
    }
}

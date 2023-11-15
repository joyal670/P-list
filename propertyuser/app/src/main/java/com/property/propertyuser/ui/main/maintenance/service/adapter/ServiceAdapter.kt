package com.property.propertyuser.ui.main.maintenance.service.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_list.Document
import com.property.propertyuser.modal.service_list.Service
import com.property.propertyuser.ui.main.home.events_see_all.adapter.EventsSeeAllItemAdapter
import com.property.propertyuser.ui.main.my_property.view_details.service_details.ServiceDetailsActivity
import com.property.propertyuser.utils.loadImagesWithGlideExt
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.list_service_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class ServiceAdapter(private val context: Context,
                     private var serviceList: ArrayList<Service>,
                     private val functionSelectedService:(Int,String)->Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable
{


    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }
    private var copyList = ArrayList<Service>()
    init {
        copyList = serviceList
    }



    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewtype: Int): RecyclerView.ViewHolder {

        return when (viewtype)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_service_item,parent,false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS ->
            {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loader_grid,parent,false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return copyList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        val viewtype = copyList[position].id
        return when(viewtype)
        {//if data is load, returns PROGRESSBAR viewtype.
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvServiceName.text =copyList[position].service
            holder.itemView.sivServiceImage.loadImagesWithGlideExt(copyList[position].image)
            /*holder.itemView.sivServiceImage.setImageResource(serviceItemIconsList[position])*/
            holder.itemView.btnChoose.setOnClickListener {
                functionSelectedService.invoke(copyList[position].id,copyList[position].service)
                //context.startActivity(Intent(context, ServiceDetailsActivity::class.java))
            }
            holder.itemView.sivServiceImage.setOnClickListener {
                StfalconImageViewer.Builder<Service>(context,copyList) { view, image ->
                    Log.e("check", Gson().toJson(image))
                    view.background= ContextCompat.getDrawable(context,R.drawable.shape_placeholder)
                    Glide.with(context)
                        .load(image.image)
                        .error(R.drawable.shape_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }.show()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(constraint : CharSequence?) : FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = serviceList
                } else {
                    val resultList = ArrayList<Service>()
                    for (row in serviceList) {
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
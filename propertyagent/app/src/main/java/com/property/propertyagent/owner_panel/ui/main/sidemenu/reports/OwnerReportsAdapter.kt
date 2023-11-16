package com.property.propertyagent.owner_panel.ui.main.sidemenu.reports

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import kotlinx.android.synthetic.main.recycle_reports_owner_list_item.view.*

class OwnerReportsAdapter(private val requestClick : (String) -> Unit) :
    RecyclerView.Adapter<OwnerReportsAdapter.ViewHold>() {
    private var context : Context? = null
    private val taskList = listOf(
        "Vacant Report" ,
        "Occupied Report" ,
        "Overall Report"
    )

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_reports_owner_list_item , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        when {
            taskList[position] == context!!.getString(R.string.vacant_report) -> {
                holder.itemView.recyerview_adapter_reports_typeTv.text =
                    context!!.getString(R.string.vacant_report)
                holder.itemView.recyerview_adapter_reports_btn.text =
                    context!!.getString(R.string.download_report)
            }
            taskList[position] == context!!.getString(R.string.occupied_report) -> {
                holder.itemView.recyerview_adapter_reports_typeTv.text =
                    context!!.getString(R.string.occupied_report)
                holder.itemView.recyerview_adapter_reports_btn.text =
                    context!!.getString(R.string.download_report)
            }
            taskList[position] == context!!.getString(R.string.overall_report) -> {
                holder.itemView.recyerview_adapter_reports_typeTv.text =
                    context!!.getString(R.string.overall_report)
                holder.itemView.recyerview_adapter_reports_btn.text =
                    context!!.getString(R.string.download_report)
            }
        }

        holder.itemView.recyerview_adapter_reports_btn.setOnClickListener {
            when {
                taskList[position] == context!!.getString(R.string.occupied_report) -> {
                    requestClick.invoke(context!!.getString(R.string.occupied_report))
                }
                taskList[position] == context!!.getString(R.string.vacant_report) -> {
                    requestClick.invoke(context!!.getString(R.string.vacant_report))
                }
                taskList[position] == context!!.getString(R.string.overall_report) -> {
                    requestClick.invoke(context!!.getString(R.string.overall_report))
                }
            }
        }
    }
}
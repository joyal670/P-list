package com.property.propertyuser.ui.main.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyuser.R
import kotlinx.android.synthetic.main.recycle_dashboard_list_item.view.*

class DashboardAdapter(
    private var dashboardNameList: ArrayList<String>,
    private var dashboardIconList: ArrayList<Int>,
    private var function: (Int) -> Unit
) : RecyclerView.Adapter<DashboardAdapter.ViewHold>() {

    private var context: Context? = null


    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_dashboard_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return dashboardNameList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        Glide.with(context!!).load(dashboardIconList[position]).into(holder.itemView.ivDashboard)
        holder.itemView.tvDashboard.text = dashboardNameList[position]

        holder.itemView.setOnClickListener {
            function.invoke(position)
        }

    }
}

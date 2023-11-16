package com.property.propertyagent.agent_panel.ui.main.home.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.modal.agent.agent_home.RentOverDue
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.CommonUtils.Companion.showUserProfile
import com.property.propertyagent.utils.CommonUtils.Companion.underline
import kotlinx.android.synthetic.main.recycle_product_name_list_item.view.*

class PropertyNameAdapter(
    private val acivity: Activity,
    private val OnclickDialer: (String) -> Unit,
    private var rentOverdueList: List<RentOverDue>,
) : RecyclerView.Adapter<PropertyNameAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_product_name_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return rentOverdueList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.PropertNameAdapter_name.text = rentOverdueList[position].user_rel.name
        val regNo: String =
            "P-" + rentOverdueList[position].prop_rel.property_reg_no.subSequence(15, 18)
                .toString()
        holder.itemView.PropertNameAdapter_propertyId.text = regNo
        holder.itemView.PropertNameAdapter_dueAmt.text = rentOverdueList[position].rent
        holder.itemView.PropertNameAdapter_propertyId.setOnClickListener {
            clicked_property_id = rentOverdueList[position].property_id.toString()
            val intent = Intent(context, PropertyViewDetailsActivity::class.java)
            intent.putExtra("property_id", clicked_property_id)
            context?.startActivity(intent)
        }

        if (rentOverdueList[position].user_rel.phone.isNotEmpty()) {
            holder.itemView.PropertNameAdapter_callBtn.setOnClickListener {
                OnclickDialer.invoke(rentOverdueList[position].user_rel.phone)
            }
        }
        holder.itemView.PropertNameAdapter_propertyId.underline()

        holder.itemView.PropertNameAdapter_name.underline()

        holder.itemView.PropertNameAdapter_name.setOnClickListener {
            showUserProfile(
                acivity,
                context!!.getString(R.string.user),
                rentOverdueList[position].user_rel.name,
                rentOverdueList[position].user_rel.phone,
                rentOverdueList[position].user_rel.profile_pic
            )
        }
    }
}
package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_property_sale_details.EmiDetail
import kotlinx.android.synthetic.main.item_rent_type_sale.view.*

class SaleDetailsAdapter(private var saleDetailsList: List<EmiDetail>) :
    RecyclerView.Adapter<SaleDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_type_sale, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return saleDetailsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.itemView.tvEmiDuration.text =
            saleDetailsList[position].emi_count.toString() + " " + saleDetailsList[position].period_type

        holder.itemView.tvEmiCalc.text =
            saleDetailsList[position].emi_count.toString() + " X SAR" + saleDetailsList[position].emi_amount.toString()

        holder.itemView.tvInterestRate.text = saleDetailsList[position].interest_rate.toString()
        holder.itemView.tvDescriptionSale.text = saleDetailsList[position].descriptions
        holder.itemView.tvTotalAmount.text = "SAR " +
                (saleDetailsList[position].emi_count.toDouble() * saleDetailsList[position].emi_amount.toDouble()).toString()
    }
}

package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.databinding.RecycleParentPaymentDetailsListItemBinding
import com.iroid.jeetmeet.modal.parent.my_account_details.ParentMyAccountDetailsFee


class ParentPaymentDetailsAdapter(
    private var requireActivity: FragmentActivity,
    private var fee: List<ParentMyAccountDetailsFee>
) :
    RecyclerView.Adapter<ParentPaymentDetailsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleParentPaymentDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = RecycleParentPaymentDetailsListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return fee.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.binding.tvId.text = "# $position"
        holder.binding.tvFeeId.text = ":" + fee[position].fee_id
        holder.binding.tvFeeType.text = fee[position].name
        holder.binding.tvAmount.text = fee[position].amount.toString()
        holder.binding.tvDue.text = "0"
        holder.binding.tvTotal.text = fee[position].amount.toString()


       /* *//* debit_from_advance *//*
        holder.binding.partiallyPay.visibility = View.GONE
        holder.binding.payNowBtn.visibility = View.GONE

        *//* pay_now *//*
        holder.binding.partiallyPay.visibility = View.GONE
        holder.binding.debitFromAdvBtn.visibility = View.GONE

        *//* pay_now, pay_partially *//*
        holder.binding.debitFromAdvBtn.visibility = View.GONE*/


    }
}

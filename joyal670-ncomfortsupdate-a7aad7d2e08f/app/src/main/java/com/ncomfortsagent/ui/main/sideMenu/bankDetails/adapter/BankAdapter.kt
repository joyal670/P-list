package com.ncomfortsagent.ui.main.sideMenu.bankDetails.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemBankBinding
import com.ncomfortsagent.model.view_bank_details.AgentBankDetail
import com.ncomfortsagent.utils.CommonUtils

class BankAdapter(
    private var requireContext: Activity,
    private var bankDetails: ArrayList<AgentBankDetail>,
    private var edit: (Int, String, String, String, String, String) -> Unit,
    private var remove: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_DATA -> {
                val view =
                    ItemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.loader, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return bankDetails.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewtype = bankDetails[position].id
        return when (viewtype) {
            -1 -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    class ViewHolder(var binding: ItemBankBinding) : RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            holder.binding.tvAccountName.text = bankDetails[position].accountname
            holder.binding.tvAccountNumber.text = bankDetails[position].accountnumber
            holder.binding.tvIFSCCode.text = bankDetails[position].ifsc
            holder.binding.tvBankName.text = bankDetails[position].bankname
            holder.binding.tvBranchName.text = bankDetails[position].branchname

            holder.binding.editAccountBtn.setOnClickListener {
                edit.invoke(
                    bankDetails[position].id,
                    bankDetails[position].accountname,
                    bankDetails[position].accountnumber,
                    bankDetails[position].ifsc,
                    bankDetails[position].bankname,
                    bankDetails[position].branchname
                )
            }

            holder.binding.removeAccountBtn.setOnClickListener {
                remove.invoke(bankDetails[position].id)
            }

            holder.binding.shareAccountBtn.setOnClickListener {
                val stringBuilder = StringBuilder()
                stringBuilder.append("Account Details\n")
                stringBuilder.append("Account Holder Name : " + bankDetails[position].accountname + "\n")
                stringBuilder.append("Account Number : " + bankDetails[position].accountnumber + "\n")
                stringBuilder.append("BIC Code: " + bankDetails[position].ifsc + "\n")
                stringBuilder.append("Bank Name : " + bankDetails[position].bankname + "\n")
                stringBuilder.append("Branch Name : " + bankDetails[position].branchname )
                CommonUtils.shareBankDetails(stringBuilder.toString(), requireContext)
            }
        }

    }

}
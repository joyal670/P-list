package com.iroid.patrickstore.ui.address.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.databinding.ItemAddressBinding
import com.iroid.patrickstore.model.addresslist.Item
import com.iroid.patrickstore.preference.AppPreferences

class AddressAdapter(
    private val context: Context,
    private val addressList: List<Item>,
    private val functionEdit: (String) -> Unit,
    private val functionDelete: (String) -> Unit,
    private val functionOnSelect: () -> Unit
) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position)
    }
    inner class ViewHolder(var binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
            binding.tvName.text = addressList[position].name
            binding.tvAddress.text = addressList[position].address1+"\n"+addressList[position].landMark
            binding.tvContactNo.text = addressList[position].contactNumber
            binding.btnType.text = addressList[position].label
            itemView.setOnClickListener {
                AppPreferences.addressId=addressList[position].id
                AppPreferences.address=addressList[position].address1
                AppPreferences.address_place=addressList[position].landMark
                AppPreferences.address_mobile=addressList[position].contactNumber
                functionOnSelect.invoke()

            }

            binding.tvEdit.setOnClickListener {
                functionEdit.invoke(addressList[position].id)
            }

            binding.tvDelete.setOnClickListener {
                functionDelete.invoke(addressList[position].id)
            }
        }
    }
}

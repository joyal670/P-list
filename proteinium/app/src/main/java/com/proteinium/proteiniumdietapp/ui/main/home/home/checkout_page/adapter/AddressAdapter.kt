package com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.get_addresses.Address
import kotlinx.android.synthetic.main.recycerview_address.view.*
import java.util.*


class AddressAdapter(private var addressList: ArrayList<Address>,val selectedAddress : (Int,String) -> Unit, val deleteAddress : (Int,Int) -> Unit, val editAddress : (Int) -> Unit) : RecyclerView.Adapter<AddressAdapter.ViewHold>()
{

    private var selectedPosition : Int = 0
    private var context: Context? = null
    private var row_index = -1

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_address, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int)
    {
        //holder.itemView.rvAddressRadioBtn.isChecked = selectedPosition == position
        if(addressList[index].default==1){
            holder.itemView.rvAddressRadioBtn.isChecked=true
        }
        val address = StringBuilder()
        if(!addressList[index].governorate.isNullOrBlank()){
            address.append(context!!.getString(R.string.governorate))
            address.append(": ")
            address.append(addressList[index].governorate)
            address.append(",")
            address.append(" ")
        }

        if(!addressList[index].area.isNullOrBlank()){
            address.append(context!!.getString(R.string.area))
            address.append(": ")
            address.append(addressList[index].area)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].block.isNullOrBlank()){
            address.append(context!!.getString(R.string.block))
            address.append(": ")
            address.append(addressList[index].block)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].street.isNullOrBlank()){
            address.append(context!!.getString(R.string.street))
            address.append(": ")
            address.append(addressList[index].street)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].avenue.isNullOrBlank()){
            address.append(context!!.getString(R.string.avenue))
            address.append(": ")
            address.append(addressList[index].avenue)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].building.toString().isNullOrBlank()){
            address.append(context!!.getString(R.string.building))
            address.append(": ")
            address.append(addressList[index].building)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].floor.toString().isNullOrBlank()){
            address.append(context!!.getString(R.string.floor))
            address.append(": ")
            address.append(addressList[index].floor)
            address.append(",")
            address.append(" ")
        }
        if(!addressList[index].appartment.toString().isNullOrBlank()){
            address.append(context!!.getString(R.string.appartment))
            address.append(": ")
            address.append(addressList[index].appartment)
        }

        holder.itemView.rvAddressTv.text = address

        holder.itemView.rvAddressRadioBtn.setOnClickListener {
            if(holder.itemView.rvAddressRadioBtn.isChecked)
            {
                row_index = index
                selectedAddress.invoke(addressList[index].id,address.toString())
                notifyDataSetChanged()
            }

        }
        if (row_index!=-1){
            holder.itemView.rvAddressRadioBtn.isChecked = row_index==index
        }
        holder.itemView.rvAddressDeleteBtn.setOnClickListener {
            deleteAddress.invoke(index,addressList[index].id)
        }

        holder.itemView.rvAddressEditBtn.setOnClickListener {
            editAddress.invoke(addressList[index].id)
        }



    }
}
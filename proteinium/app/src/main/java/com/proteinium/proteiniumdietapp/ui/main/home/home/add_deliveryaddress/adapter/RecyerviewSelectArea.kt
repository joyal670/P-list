package com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.areas.Area
import kotlinx.android.synthetic.main.recycerview_address_layout.view.*


class RecyerviewSelectArea (private var areaList: ArrayList<Area>,
                            private val funSelectedArea: (Int) -> Unit) : RecyclerView.Adapter<RecyerviewSelectArea.ViewHold>()
{

    private var selectedPosition : Int = 0
    private var context: Context? = null
    private var row_index = -1

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_address_layout, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return areaList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.radioDialog.isChecked = areaList[position].isChecked
        holder.itemView.tvDialogAddress.text = areaList[position].name

        holder.itemView.llGovernorate.setOnClickListener {

            for (i in 0 until areaList.size-1) {
                areaList[i].isChecked = areaList[position].id==areaList[i].id
            }

            funSelectedArea.invoke(position)
            notifyDataSetChanged()

        }


    }
}
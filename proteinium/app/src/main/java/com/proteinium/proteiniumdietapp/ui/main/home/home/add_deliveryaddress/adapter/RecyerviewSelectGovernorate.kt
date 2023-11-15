package com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.governorate.Governorates
import kotlinx.android.synthetic.main.recycerview_address_layout.view.*


class RecyerviewSelectGovernorate (private var governorsList: ArrayList<Governorates>,
                                   private val funSelectedGov: (Int) -> Unit) : RecyclerView.Adapter<RecyerviewSelectGovernorate.ViewHold>()
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
        return governorsList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.radioDialog.isChecked = governorsList[position].isChecked
        holder.itemView.tvDialogAddress.text = governorsList[position].name

        holder.itemView.llGovernorate.setOnClickListener {

            for (i in 0 until governorsList.size-1) {
                governorsList[i].isChecked = governorsList[position].id==governorsList[i].id
            }

            funSelectedGov.invoke(position)
            notifyDataSetChanged()

        }


    }
}
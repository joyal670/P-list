package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.meal_plan.ExtrasModified
import com.proteinium.proteiniumdietapp.utils.toPrice
import kotlinx.android.synthetic.main.recycerview_extras.view.*


class ExtrasAdapter(
    private var extras: List<ExtrasModified>,
    private var addData: (String, String, String, String, String) -> Unit,
    private var remove:(String)->Unit,
    private var duration: Int
) : RecyclerView.Adapter<ExtrasAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycerview_extras, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return extras.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        holder.itemView.materialRadioButton.text=extras[index].name
        var intCounter = 0
        holder.itemView.addBtn.setOnClickListener {
            if(holder.itemView.materialRadioButton.isChecked) {
                intCounter++
                if(intCounter<extras[index].extras_modify.size){
                    addData.invoke(extras[index].id,extras[index].id+"_"+extras[index].extras_modify[intCounter].quantity+"_"+extras[index].extras_modify[intCounter].price,
                        extras[index].extras_modify[intCounter].price,"${extras[index].name} " +
                            "${extras[index].extras_modify[intCounter].quantity}",
                        extras[index].extras_modify[intCounter].price)
                    holder.itemView.countBtn.text = extras[index].extras_modify[intCounter].quantity
                    holder.itemView.tvPrice.text = extras[index].extras_modify[intCounter].price.toString().toDouble().toPrice()
                }else{
                    intCounter--
                }

            }
        }
        holder.itemView.removeBtn.setOnClickListener {
            if(holder.itemView.materialRadioButton.isChecked){
                if(intCounter!=0){
                    intCounter--
                    addData.invoke(extras[index].id,extras[index].id+"_"+extras[index].extras_modify[intCounter].quantity+"_"+extras[index].extras_modify[intCounter].price,
                        extras[index].extras_modify[intCounter].price,"${extras[index].name} " +
                            "${extras[index].extras_modify[intCounter].quantity}",
                        extras[index].extras_modify[intCounter].price)
                    holder.itemView.countBtn.text = extras[index].extras_modify[intCounter].quantity
                    holder.itemView.tvPrice.text = extras[index].extras_modify[intCounter].price.toString().toDouble().toPrice()
                }

            }

        }

        holder.itemView.materialRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                addData.invoke(extras[index].id,extras[index].id+"_"+extras[index].extras_modify[intCounter].quantity+"_"+extras[index].extras_modify[intCounter].price,
                    extras[index].extras_modify[intCounter].price,"${extras[index].name} " +
                        "${extras[index].extras_modify[intCounter].quantity}",
                    extras[index].extras_modify[intCounter].price)
                holder.itemView.countBtn.text = extras[index].extras_modify[intCounter].quantity
                holder.itemView.tvPrice.text = extras[index].extras_modify[intCounter].price.toString().toDouble().toPrice()
            }else{
                remove.invoke(extras[index].id)
                holder.itemView.countBtn.text = "0"
                holder.itemView.tvPrice.text = "0".toDouble().toPrice()

            }
        }



    }

    fun durationValue(durationArg: Int) {

    }
}

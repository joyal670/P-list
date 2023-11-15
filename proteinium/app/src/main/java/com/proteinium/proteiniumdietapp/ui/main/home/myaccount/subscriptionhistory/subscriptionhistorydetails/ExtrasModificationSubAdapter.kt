package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.meal_plan.ExtrasModified
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.Extra
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.ExtraModify
import com.proteinium.proteiniumdietapp.utils.toPriceRound
import kotlinx.android.synthetic.main.item_extras_modify_main.view.*
import kotlinx.android.synthetic.main.recycerview_address_layout.view.*
import okhttp3.internal.notify


class ExtrasModificationSubAdapter(
    private val extras: ArrayList<ExtraModify>,
    private val function: (String,String,String,String) -> Unit,
    private val notify:()->Unit,
    private val remove:(String)->Unit
) : RecyclerView.Adapter<ExtrasModificationSubAdapter.ViewHold>() {

    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_extras_modify_main, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return extras.size
    }

    override fun onBindViewHolder(holder: ViewHold, index: Int) {
        holder.itemView.tvQty.text = extras[index].quantity
        holder.itemView.tvPrice.text = "${context!!.getString(R.string.kwd)+  " " + extras[index].price.toDouble().toPriceRound()}"
        holder.itemView.tvQty.isChecked = extras[index].isChecked
        Log.e("$34345","$extras")




        holder.itemView.setOnClickListener {
            extras.forEach {
                if(extras[index].quantity==it.quantity){
                    if(extras[index].isChecked){
                        extras[index].isChecked=false
                        remove.invoke(extras[index].id)
                    }else{
                        extras[index].isChecked=true
                        function.invoke(extras[index].id,extras[index].id+"_"+extras[index].quantity+"_"+extras[index].price,
                            extras[index].price,"${extras[index].name} " +
                                "${extras[index].quantity}"
                        )
                    }

                }else{
                    it.isChecked=false
                }
                notifyDataSetChanged()

            }

        }
    }
}

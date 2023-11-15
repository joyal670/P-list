package com.iroid.patrickstore.ui.shop_details.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.service.service_list.Item
import com.iroid.patrickstore.ui.shop_details.services.service_details.ServiceDetailsActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants
import kotlinx.android.synthetic.main.recycle_shop_service_item.view.*
import java.io.Serializable

class ShopServiceAdapter(
    private val context: Context,
    private val serviceList: List<Item>,
    private val function: (Item) -> Unit
): RecyclerView.Adapter<ShopServiceAdapter.ViewHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_shop_service_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
       return serviceList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setViews(serviceList[position],context)
        holder.itemView.setOnClickListener {
            val intent= Intent(context,ServiceDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_SERVICE,serviceList[position] as Serializable)
            context.startActivity(intent)
        }
    }


    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setViews(item: Item, context: Context) {
            try {
                CommonUtils.setImageBase(context,item.imgUrl[0].publicUrl,itemView.ivServiceBanner)
            }catch (ex:Exception){


            }

            itemView.textView7.text=item.serviceName
            itemView.tvDescription.text=item.description
            itemView.tvRate.text=item.maxPrice.toString()
            itemView.tvRupeeSymbol.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tvCrossedRate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }


}

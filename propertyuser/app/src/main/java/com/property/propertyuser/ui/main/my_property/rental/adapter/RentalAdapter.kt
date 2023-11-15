package com.property.propertyuser.ui.main.my_property.rental.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.my_property_list.BookedData
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.pojo.ItemEventModelData
import com.property.propertyuser.pojo.PropertyTypeModelData
import com.property.propertyuser.ui.main.home.adapter.HomeEventItemAdapter
import com.property.propertyuser.ui.main.my_property.view_details.view_payment_history.adapter.PaymentHistoryAdapter
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_event_items.view.*
import kotlinx.android.synthetic.main.list_property_type_items.view.*
import kotlinx.android.synthetic.main.list_rental_item.view.*

class RentalAdapter(private val context: Context,
                    private  var rentalPropertyList: List<BookedProperty>,
                    private  val functionService: (Int,Int) -> Unit,
                    private  val functionVacate: (Int) -> Unit,
                    private  val functionViewDetails: (Int) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return when (viewType)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_rental_item,parent,false)
                ViewHolder(view)
            }
            VIEW_TYPE_PROGRESS ->
            {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loader,parent,false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = rentalPropertyList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }
    override fun getItemCount(): Int {
        return rentalPropertyList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvPropertyName.text=rentalPropertyList[position].property_name
            holder.itemView.tvPropertyCode.text =
                context.getString(R.string.tvPropertyCode)+" "+rentalPropertyList[position].property_reg_no
            holder.itemView.tvRentAmount.text=
                rentalPropertyList[position].rent
            if(rentalPropertyList[position].property_priority_image!=null){
                holder.itemView.roundedRentalPropertyImage.loadImagesWithGlideExt(rentalPropertyList[position].property_priority_image.document)
            }
            if(rentalPropertyList[position].user_property_related!=null){
                holder.itemView.tvNextDueDate.text=
                    context.getString(R.string.tvNextDueDate)+" "+
                            rentalPropertyList[position].due_date
            }
            holder.itemView.btnServiceRequest.setOnClickListener {
                if(rentalPropertyList[position].user_property_related!=null){
                    functionService.invoke(rentalPropertyList[position].user_property_related.property_id,
                    rentalPropertyList[position].user_property_related.id)
                }
            }
            holder.itemView.btnVacateRequest.setOnClickListener {
                functionVacate.invoke(rentalPropertyList[position].user_property_related.id)
            }
            holder.itemView.tvViewDetails.setOnClickListener {
                functionViewDetails.invoke(rentalPropertyList[position].user_property_related.id)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
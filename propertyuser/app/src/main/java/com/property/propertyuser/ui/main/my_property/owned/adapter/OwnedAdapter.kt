package com.property.propertyuser.ui.main.my_property.owned.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyuser.R
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.ui.main.my_property.rental.adapter.RentalAdapter
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.list_owned_item.view.*

class OwnedAdapter(private val context: Context,
                   private  var ownedPropertyList: List<BookedProperty>,
                   private val functionServiceOwned: (Int,Int) -> Unit,
                   private  val functionViewDetailsOwner: (Int) -> Unit)
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
                    .inflate(R.layout.list_owned_item,parent,false)
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

    override fun getItemCount(): Int {
        return ownedPropertyList.size
    }
    override fun getItemViewType(position: Int): Int
    {
        var viewtype = ownedPropertyList[position].id
        return when(viewtype)
        {
            -1  -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        if (holder is ViewHolder){
            holder.itemView.tvPropertyName.text=ownedPropertyList[position].property_name
            holder.itemView.tvPropertyCodeOwned.text =
                context.getString(R.string.tvPropertyCode)+" "+ownedPropertyList[position].property_reg_no
            if(ownedPropertyList[position].property_priority_image!=null){
                holder.itemView.roundedPropertyOwnedImage.
                loadImagesWithGlideExt(ownedPropertyList[position].property_priority_image.document)
            }
            if(ownedPropertyList[position].user_property_related!=null){
                holder.itemView.tvNextDueDate.text=
                    context.getString(R.string.tvNextDueDate)+" "+ownedPropertyList[position].due_date
            }
            holder.itemView.tvRentAmountOwned.text=
                context.getString(R.string.sar)+" "+ownedPropertyList[position].selling_price
            holder.itemView.btnServiceRequestOwned.setOnClickListener {
                if(ownedPropertyList[position].user_property_related!=null){
                    functionServiceOwned.invoke(ownedPropertyList[position].user_property_related.property_id,
                    ownedPropertyList[position].user_property_related.id)
                }
            }

            holder.itemView.tvViewDetailsOwned.setOnClickListener {
                Log.e("check owner view",ownedPropertyList[position].user_property_related.id.toString())
                functionViewDetailsOwner.invoke(ownedPropertyList[position].user_property_related.id)
            }
        }
        //holder.itemView.tvPropertyName.text =ownedPropertyList[position].

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    class ProgressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }
}
package com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import kotlinx.android.synthetic.main.recycer_main_pdetails.view.*

class pDetailsAdapter(
    private var amenityDetail : ArrayList<Amenity_Detail> ,
    private var getNoText : (Int , Int) -> Unit
) : RecyclerView.Adapter<pDetailsAdapter.ViewHold>() {
    private var context : Context? = null

    class ViewHold(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(com.property.propertyagent.R.layout.recycer_main_pdetails , parent , false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount() : Int {
        return amenityDetail.size
    }

    override fun onBindViewHolder(holder : ViewHold , position : Int) {
        //holder.itemView.pDetailsEtx.hint = amenityDetail[position].placeholder
        holder.itemView.textInputLayout.hint = amenityDetail[position].placeholder
//        holder.itemView.pDetailsEtx.doOnTextChanged { text, start, before, count ->
//            getNoText.invoke(amenityDetail[position].detail_id,holder.itemView.pDetailsEtx.text.toString())
//
//        }
        holder.itemView.pDetailsEtx.doAfterTextChanged {
            if (it != null) {
                if (it.length <= 4 && !it.isNullOrEmpty())
                    getNoText.invoke(
                        amenityDetail[position].detail_id ,
                        holder.itemView.pDetailsEtx.text.toString().toInt()
                    )
            }
        }
    }
}



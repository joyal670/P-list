package com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subCatagory
import kotlinx.android.synthetic.main.recycer_amenities.view.*


class AnimenitiesSubAdapter(private var amenities : List<Amenity_subCatagory> , private var addItem : (Int) ->Unit , private var remove : (Int) ->Unit) : RecyclerView.Adapter<AnimenitiesSubAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(com.property.propertyagent.R.layout.recycer_amenities, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return amenities.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        context?.let { Glide.with(it).load(amenities[position].image).into(holder.itemView.ivAmenities) }

        holder.itemView.typeAmenities.text = amenities[position].name

        holder.itemView.ChkAmenities.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                addItem.invoke(amenities[position].id)
            }
            else
            {
                remove.invoke(amenities[position].id)
            }
        }

    }
}

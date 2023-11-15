package com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import kotlinx.android.synthetic.main.recycer_main_amenities.view.*

class AnimenitiesAdapter(private var AnimenitiesList : ArrayList<Amenity_subTitle> ,
                         private var addItem : (Int) -> Unit , private var removeItem : (Int) -> Unit) :
    RecyclerView.Adapter<AnimenitiesAdapter.ViewHold>()
{
    private var context: Context? = null

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(com.property.propertyagent.R.layout.recycer_main_amenities, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return AnimenitiesList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        holder.itemView.tv_mainAmineity.text = AnimenitiesList[position].name
        holder.itemView.rvSubAmenities.layoutManager = LinearLayoutManager(context)
        holder.itemView.rvSubAmenities.adapter = AnimenitiesSubAdapter(AnimenitiesList[position].amenities,{addItem(it)}) { removeItem(it)}


    }

    private fun removeItem(pos: Int) {
        removeItem.invoke(pos)
    }

    private fun addItem(pos: Int) {
        addItem.invoke(pos)
    }
}


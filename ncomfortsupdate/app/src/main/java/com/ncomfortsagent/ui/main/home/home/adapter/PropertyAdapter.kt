package com.ncomfortsagent.ui.main.home.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.R
import com.ncomfortsagent.databinding.ItemBuildingBinding
import com.ncomfortsagent.databinding.ItemPropertyBinding
import com.ncomfortsagent.model.property.PropertyNewUserProperty
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.loadImageToCoil
import java.util.*

class PropertyAdapter(
    private var requireContext: Activity,
    private var selectedItem: (Int, Int, Int) -> Unit,
    private var myProperty: ArrayList<PropertyNewUserProperty>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var copyList = ArrayList<PropertyNewUserProperty>()

    init {
        copyList = myProperty
    }

    companion object {
        const val VIEW_TYPE_INDIVIDUAL = 0
        const val VIEW_TYPE_BUILDING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        return when (viewtype) {
            VIEW_TYPE_INDIVIDUAL -> {
                val view =
                    ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PropertyViewHolder(view)
            }
            VIEW_TYPE_BUILDING -> {
                val view =
                    ItemBuildingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BuildingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (copyList[position].is_builder) {
            0 -> VIEW_TYPE_INDIVIDUAL
            1 -> VIEW_TYPE_BUILDING
            else -> VIEW_TYPE_INDIVIDUAL
        }
    }

    class PropertyViewHolder(var binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root)

    class BuildingViewHolder(var binding: ItemBuildingBinding) :
        RecyclerView.ViewHolder(binding.root)


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PropertyViewHolder) {

            holder.binding.imageView3.loadImageToCoil(copyList[position].property_priority_image.document)

            holder.binding.tvPropertyName.text = copyList[position].property_name
            holder.binding.tvDetails.text = copyList[position].feature_details

            if (copyList[position].property_to == 0) {
                holder.binding.tvAmount.text =
                    requireContext.getString(R.string.sar) + copyList[position].rent
            } else {
                holder.binding.tvAmount.text =
                    requireContext.getString(R.string.sar) + copyList[position].mrp
            }


            when (copyList[position].occupied) {
                0 -> {
                    holder.binding.tvStatus.text = requireContext.getString(R.string.vacant)
                    holder.binding.tvStatus.setTextColor(Color.parseColor("#EABC6B"))
                }
                1 -> {
                    holder.binding.tvStatus.text = requireContext.getString(R.string.occupied)
                    holder.binding.tvStatus.setTextColor(Color.parseColor("#6AC58C"))
                }

            }


            holder.itemView.setOnClickListener {
                if (CommonUtils.isOpenRecently()) return@setOnClickListener
                selectedItem.invoke(copyList[position].id, 0, copyList[position].occupied)
            }
        }

        if (holder is BuildingViewHolder) {

            holder.binding.imageView3.loadImageToCoil(copyList[position].property_priority_image.document)
            holder.binding.tvPropertyName.text = copyList[position].property_name
            holder.binding.tvApartments.text =
                requireContext.getString(R.string.noOf) + " " + copyList[position].type_details.type + " : " + copyList[position].appartments_count.toString()
            holder.binding.tvOccupied.text =
                requireContext.getString(R.string.occupiedFull) + copyList[position].occupied_count.toString()
            holder.binding.tvVacant.text =
                requireContext.getString(R.string.vacantFull) + copyList[position].vacated_count.toString()

            val gridLayoutManager = GridLayoutManager(requireContext, 3)
            gridLayoutManager.orientation = GridLayoutManager.VERTICAL
            holder.binding.rvSubBuilding.layoutManager = gridLayoutManager

            if (copyList[position].building_apartments.isEmpty()) {
                holder.binding.rvSubBuilding.visibility = View.GONE
            } else {
                holder.binding.rvSubBuilding.visibility = View.VISIBLE
                holder.binding.rvSubBuilding.adapter =
                    BuildingSubAdapter(copyList[position].building_apartments, requireContext)
            }

            if (copyList[position].building_apartments.isEmpty()) {
                holder.binding.hideBtn.visibility = View.GONE
            }
            holder.binding.hideBtn.setOnClickListener {
                if (CommonUtils.isOpenRecently()) return@setOnClickListener
                val temp = holder.binding.hideBtn.text.toString()
                if (temp == requireContext.getString(R.string.hide)) {
                    holder.binding.expandableLayout.collapse()
                    // holder.binding.rvSubBuilding.visibility = View.GONE
                    holder.binding.hideBtn.setText(R.string.show)
                } else {
                    holder.binding.expandableLayout.expand()
                    // holder.binding.rvSubBuilding.visibility = View.VISIBLE
                    holder.binding.hideBtn.setText(R.string.hide)
                }
            }

            holder.binding.viewDetailsBtn.setOnClickListener {
                if (CommonUtils.isOpenRecently()) return@setOnClickListener
                selectedItem.invoke(copyList[position].id, 1, copyList[position].occupied)
            }
            holder.itemView.setOnClickListener {
                if (CommonUtils.isOpenRecently()) return@setOnClickListener
                selectedItem.invoke(copyList[position].id, 1, copyList[position].occupied)
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = myProperty
                } else {
                    val resultList = ArrayList<PropertyNewUserProperty>()
                    for (row in myProperty) {
                        if (row.property_name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    copyList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = copyList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                copyList = results?.values as ArrayList<PropertyNewUserProperty>
                notifyDataSetChanged()
            }
        }
    }

}
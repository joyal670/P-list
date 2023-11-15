package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingBuildingApartment
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.activity.PropertyOwnerBuildingDetailedActivity
import com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details.PropertyMainDetailedActivity
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import kotlinx.android.synthetic.main.owner_building.view.*
import kotlinx.android.synthetic.main.owner_property.view.*
import java.util.*
import kotlin.collections.ArrayList

class PropertyMainAdapterOwner(
    context: Context?,
    private var propertyList: ArrayList<OwnerPropertyMainListingOwner>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    companion object {
        const val VIEW_TYPE_ONE = 0
        const val VIEW_TYPE_TWO = 1
    }

    private var context: Context? = context
    private var copyList = ArrayList<OwnerPropertyMainListingOwner>()

    init {
        copyList = propertyList
    }

    private inner class View1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            //itemView.rvPropretyList.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
            //itemView.rvPropretyList.adapter = PropertyBuildingOneAdapter(position, copyList)

            itemView.tvPropertyName.text = copyList[position].property_name
            Glide.with(context!!).load(copyList[position].property_priority_image.document)
                .into(itemView.ivPropertyImage)
            itemView.tvPropertyCode.text = copyList[position].property_reg_no
            if (copyList[position].category == 1) {
                itemView.tvPropertyCatagory.text = context!!.getString(R.string.commercial)
            } else {
                itemView.tvPropertyCatagory.text = context!!.getString(R.string.residential)
            }
            itemView.tvNoOfApartment.text = copyList[position].appartments_count.toString()
            itemView.tvNetWorth.text = copyList[position].net_worth
            itemView.tvIncome.text = copyList[position].income
            itemView.tvOutStandingDue.text = copyList[position].outstanding_due
            itemView.tvPending.text = copyList[position].pending

            if (copyList[position].property_to == 1) {
                itemView.tvPropertyType.text = context!!.getString(R.string.sale)
            } else {
                itemView.tvPropertyType.text = context!!.getString(R.string.rent)
            }

            if (copyList[position].property_to == 1) {
                itemView.layoutRevenue.isVisible = false
                itemView.tvPropertyType.text = context!!.getString(R.string.sale)
            } else if (copyList[position].occupied != 1) {
                itemView.layoutRevenue.isVisible = false
            }

            itemView.owner_building_recycerview_view_details.setOnClickListener {
                clicked_property_id = copyList[position].id.toString()
                context?.startActivity(Intent(context, PropertyMainDetailedActivity::class.java))
            }
        }
    }

    private inner class View2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            //itemView.rvPropretyBuildingList.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
            //itemView.rvPropretyBuildingList.adapter = PropertyBuidingTwoAdapter(position, copyList)

            itemView.tvBuildingName.text = copyList[position].property_name
            Glide.with(context!!).load(copyList[position].property_priority_image.document)
                .into(itemView.ivBuilding)
            itemView.tvBuildingNoOfApartment.text =
                copyList[position].appartments_count.toString()
            itemView.tvNoOfOccupied.text = copyList[position].occupied.toString()
            itemView.tvNoOfVacant.text = copyList[position].vacated.toString()
            itemView.tvBuildingNetWorth.text = copyList[position].net_worth
            itemView.tvBuildingIncome.text = copyList[position].income
            itemView.tvBuildingOutStandingDue.text = copyList[position].outstanding_due
            itemView.tvBuildingPending.text = copyList[position].pending


            val gridLayoutManager = GridLayoutManager(context, 3)
            gridLayoutManager.orientation = GridLayoutManager.VERTICAL
            itemView.rvOwnerBuildingImages.layoutManager = gridLayoutManager
            if (copyList[position].bulding_appartments.isEmpty()) {
                //itemView.llEmptyData.visibility = View.VISIBLE
                itemView.hideApartmentBtn.visibility = View.GONE
                val emptyArrayList = ArrayList<OwnerPropertyMainListingBuildingApartment>()
                itemView.rvOwnerBuildingImages.adapter = BuildingSubImagesAdapter(emptyArrayList)
            } else {
                //itemView.llEmptyData.visibility = View.GONE
                itemView.hideApartmentBtn.visibility = View.VISIBLE
                itemView.rvOwnerBuildingImages.adapter =
                    BuildingSubImagesAdapter(copyList[position].bulding_appartments)
            }

            itemView.hideApartmentBtn.setOnClickListener {
                val str = itemView.hideApartmentBtn.text.toString()
                if (str == context!!.getString(R.string.hide_appartment)) {
                    itemView.rvOwnerBuildingImages.visibility = View.GONE
                    itemView.hideApartmentBtn.text = context!!.getString(R.string.show_appartment)
                } else {
                    itemView.rvOwnerBuildingImages.visibility = View.VISIBLE
                    itemView.hideApartmentBtn.text = context!!.getString(R.string.hide_appartment)
                }
            }

            if (copyList[position].property_to == 1) {
                itemView.tvPropertyType1.text = context!!.getString(R.string.sale)
            } else {
                itemView.tvPropertyType1.text = context!!.getString(R.string.rent)
            }

            if (copyList[position].category == 0 ){
                itemView.tvPropertyCatagory1.text = context!!.getString(R.string.residential)
            } else {
                itemView.tvPropertyCatagory1.text = context!!.getString(R.string.commercial)
            }

            itemView.owner_property_image_viewDetailsbtn.setOnClickListener {
                val intent = Intent(context!!, PropertyOwnerBuildingDetailedActivity::class.java)
                intent.putExtra("selectedId", copyList[position].id)
                context!!.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.owner_property, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.owner_building, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return copyList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (copyList[position].is_builder == VIEW_TYPE_ONE) {
            (holder as View1ViewHolder).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return copyList[position].is_builder
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    copyList = propertyList
                } else {
                    val resultList = ArrayList<OwnerPropertyMainListingOwner>()
                    for (row in propertyList) {
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
                copyList = results?.values as ArrayList<OwnerPropertyMainListingOwner>
                notifyDataSetChanged()
            }
        }
    }
}
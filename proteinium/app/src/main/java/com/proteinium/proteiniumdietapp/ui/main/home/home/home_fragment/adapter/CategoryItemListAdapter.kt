package com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.home.MealCategory
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import kotlinx.android.synthetic.main.list_home_item.view.*

class CategoryItemListAdapter(
    private val context: Context,
    private var mealCategoryList: ArrayList<MealCategory>,
    private val function: (Int) -> Unit
)
    : RecyclerView.Adapter<CategoryItemListAdapter.ViewHolder>(){


    private var firstClick:Boolean=true
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home_item,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return mealCategoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        CommonUtils.setImage(context,mealCategoryList[position].image,holder.itemView.ivItemImage)


        holder.itemView.tvCategory.text=mealCategoryList[position].name
        if(mealCategoryList[position].meal_plans.isNotEmpty()){
            holder.itemView.rvCategorySubListItems.layoutManager = LinearLayoutManager(context)
            holder.itemView.rvCategorySubListItems.adapter= SubCategoryItemContentListAdapter(context,mealCategoryList[position].meal_plans)
            holder.itemView.rvCategorySubListItems.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
        holder.itemView.fabArrowDown.setOnClickListener {
            if(mealCategoryList[position].meal_plans.isNotEmpty()){
                fab(holder,position)

            }
        }
        holder.itemView.fabArrowDown1.setOnClickListener {
            if(mealCategoryList[position].meal_plans.isNotEmpty()){
                fabOne(holder,position)
                function.invoke(position)
            }
        }
        holder.itemView.setOnClickListener {
            if(firstClick){
                if(mealCategoryList[position].meal_plans.isNotEmpty()){
                    fabOne(holder,position)
                    firstClick=false
                }
            }
            else{
                if(mealCategoryList[position].meal_plans.isNotEmpty()){
                    fab(holder,position)
                    firstClick=true
                }
            }
        }
    }

    private fun fab(
        holder: ViewHolder,
        position: Int
    ) {
        if(holder.itemView.ivArrowDown.rotation==90f){
            holder.itemView.ivArrowDown.rotation=270f
           // holder.itemView.rvCategorySubListItems.visibility=View.VISIBLE
            holder.itemView.expandable_layout.expand()
            holder.itemView.fabArrowDown.visibility=View.VISIBLE
            holder.itemView.ivArrowDown.visibility=View.VISIBLE
            holder.itemView.fabArrowDown1.visibility=View.GONE
            holder.itemView.ivArrowDown1.visibility=View.GONE
        }
        else if(holder.itemView.ivArrowDown.rotation==270f){
            holder.itemView.ivArrowDown.rotation=90f
           // holder.itemView.rvCategorySubListItems.visibility=View.GONE
            holder.itemView.expandable_layout.collapse()
            holder.itemView.fabArrowDown.visibility=View.GONE
            holder.itemView.ivArrowDown.visibility=View.GONE
            holder.itemView.fabArrowDown1.visibility=View.VISIBLE
            holder.itemView.ivArrowDown1.visibility=View.VISIBLE
        }
    }

    private fun fabOne(
        holder: ViewHolder,
        position: Int
    ) {
        if(holder.itemView.ivArrowDown1.rotation==90f){
            holder.itemView.ivArrowDown.rotation=270f
            //holder.itemView.rvCategorySubListItems.visibility=View.VISIBLE
            holder.itemView.expandable_layout.expand()

            holder.itemView.fabArrowDown.visibility=View.VISIBLE
            holder.itemView.ivArrowDown.visibility=View.VISIBLE
            holder.itemView.fabArrowDown1.visibility=View.GONE
            holder.itemView.ivArrowDown1.visibility=View.GONE

        }
        else if(holder.itemView.ivArrowDown1.rotation==270f){
            holder.itemView.ivArrowDown.rotation=90f
            //holder.itemView.rvCategorySubListItems.visibility=View.GONE
            holder.itemView.expandable_layout.collapse()
            holder.itemView.fabArrowDown.visibility=View.GONE
            holder.itemView.ivArrowDown.visibility=View.GONE
            holder.itemView.fabArrowDown1.visibility=View.VISIBLE
            holder.itemView.ivArrowDown1.visibility=View.VISIBLE
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
package com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.home.MealPlan
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.MealPlanActivity
import com.proteinium.proteiniumdietapp.utils.Constants
import kotlinx.android.synthetic.main.list_category_sub_items.view.*

class SubCategoryItemContentListAdapter (private val context: Context,private var mealPlanList:List<MealPlan>)
    : RecyclerView.Adapter<SubCategoryItemContentListAdapter.ViewHolder>(){

   /* private  val subCategoryItemContentList= listOf<String>("Three Meal Per Day",
        "Three Meal Per Day",
        "Three Meal Per Day")*/
   var combineItems:String=""
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category_sub_items,parent,false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return mealPlanList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        holder.itemView.tvCategoryItemTitle1.text=mealPlanList[position].name

        holder.itemView.tvCategoryItems.text=mealPlanList[position].short_description

        holder.itemView.setOnClickListener {
            /*if(isLogin){*/
                val intent=Intent(context,MealPlanActivity::class.java)
                intent.putExtra(Constants.INTENT_MEAL_ID,mealPlanList[position].id)
                context.startActivity(intent)
            /*}else{
                val intent=Intent(context,AuthActivity::class.java)
                context.startActivity(intent)
            }*/



        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}
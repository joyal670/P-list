package com.iroid.patrickstore.ui.home.adapter.navadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.all_categories.AllCategories
import kotlinx.android.synthetic.main.item_category.view.*


class CategoriesNavAdapter(private val context: Context, private  val categoriesList:List<AllCategories>, private val function: (String, Boolean,String) -> Unit) : RecyclerView.Adapter<CategoriesNavAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_side, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val isExpanded: Boolean = categoriesList[position].expanded
        holder.itemView.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        if(isExpanded){
            holder.itemView.tvCategories.setTypeface(null, Typeface.BOLD)
        }else{
            holder.itemView.tvCategories.setTypeface(null, Typeface.NORMAL)
        }

        holder.setupViews(categoriesList[position],function)
        if(categoriesList[position].isChecked){
            holder.itemView.tvCategories.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_up_24,
                0
            )
        }else{
            holder.itemView.tvCategories.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic__arrow_down_24,
                0
            )
        }
        holder.itemView.rvSub.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        holder.itemView.rvSub.adapter=SubCategoriesNavAdapter(context,categoriesList[position].children) {
                id, qty,name ->function.invoke(id,qty,name)
        }
        holder. itemView.setOnClickListener {
            var i=0
            categoriesList.forEach {
                if(it.name==categoriesList[position].name){
                    categoriesList[i].isChecked = !categoriesList[i].isChecked
                    }
                i++
            }
            categoriesList[position].expanded = !categoriesList[position].expanded
            notifyItemChanged(position)
//            notifyDataSetChanged()
//            function.invoke(categoriesList[position]._id,categoriesList[position].isPerishable)
        }

    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setupViews(allCategories: AllCategories, function: (String, Boolean,String) -> Unit) {
            itemView.tvCategories.text=allCategories.name

        }
    }
}
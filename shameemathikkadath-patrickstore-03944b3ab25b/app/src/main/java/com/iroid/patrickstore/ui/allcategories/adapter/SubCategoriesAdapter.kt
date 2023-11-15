package com.iroid.patrickstore.ui.allcategories.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.all_categories.ChildrenCategories
import kotlinx.android.synthetic.main.item_category.view.*


class SubCategoriesAdapter(private val context: Context, private val categoriesList:List<ChildrenCategories>, private val function: (String, Boolean, Any?) -> Unit) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_main, parent, false)
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
        holder.setupViews(categoriesList[position],function)
        if(categoriesList[position].isChecked){
           holder.itemView.constraintCategory.setBackgroundColor(context.getColor(R.color.colorBg))
        }else{
            holder.itemView.constraintCategory.setBackgroundColor(context.getColor(R.color.colorWhite))
        }
        holder. itemView.setOnClickListener {
            var i=0
            categoriesList.forEach {
                categoriesList[i].isChecked = it.name==categoriesList[position].name
                i++
            }
            notifyDataSetChanged()
            function.invoke(categoriesList[position]._id,categoriesList[position].isPerishable,categoriesList[position].name)
        }
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setupViews(allCategories: ChildrenCategories, function: (String, Boolean, Any?) -> Unit) {
            itemView.tvCategories.text=allCategories.name

        }
    }
}

package com.proteinium.proteiniumdietapp.ui.main.home.calender.fragment.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.subscption_info.Food
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.stfalcon.imageviewer.StfalconImageViewer
import com.willy.ratingbar.ScaleRatingBar
import kotlinx.android.synthetic.main.recycerview_calender.view.*
import kotlinx.android.synthetic.main.recycerview_calender.view.llratingFood
import kotlinx.android.synthetic.main.recycerview_calender.view.ratingFood
import kotlinx.android.synthetic.main.recycerview_calender.view.tvAverage
import render.animations.Fade
import render.animations.Render


class CalenderAdapter(
    private val foodList: List<Food>,
    private val context: Context,
    private val function: (Int,Int)->Unit
) : RecyclerView.Adapter<CalenderAdapter.ViewHold>()
{
    private var imageSliderList = ArrayList<String>()

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycerview_calender, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        val render = Render(context!!)

        holder.itemView.tvRateFood.paintFlags = holder.itemView.tvRateFood.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.itemView.ingredientsBtn.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_drop_down_white, 0)
        holder.itemView.ingredientsBtn.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
        CommonUtils.setImage(context,foodList[position].image,holder.itemView.productImage)
        holder.itemView.tvProductName.text=foodList[position].name
        val dec=StringBuilder()
        dec.append(foodList[position].description)
        dec.append("\n")
        dec.append("\n")
        dec.append(context.getString(R.string.protein_100g))
        dec.append(":")
        dec.append("\t")
        dec.append(foodList[position].proteins)
        dec.append(context.getString(R.string.gram))
        dec.append("\t")
        dec.append("\t")
        dec.append(context.getString(R.string.carbs))
        dec.append(":")
        dec.append("\t")
        dec.append(foodList[position].carbs)
        dec.append(context.getString(R.string.gram))
        dec.append("\t")
        dec.append("\t")
        dec.append(context.getString(R.string.fat))
        dec.append(":")
        dec.append("\t")
        dec.append(foodList[position].fat)
        dec.append(context.getString(R.string.gram))
        dec.append("\t")
        dec.append("\t")
        dec.append(context.getString(R.string.calories))
        dec.append(":")
        dec.append("\t")
        dec.append(foodList[position].calories)
        dec.append("\t")
        holder.itemView.tvCalenderExpandText.text=dec.toString()
        holder.itemView.ratingFood.rating= foodList[position].my_rating.toFloat()
        val rating=StringBuilder()
        if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
            holder.itemView.llratingFood.rotationY=-360f
            holder.itemView.tvAverage.rotationY=-360f
            rating.append(5)
            rating.append("/")
            rating.append(foodList[position].my_rating.toInt())

        }else{
            rating.append(foodList[position].my_rating.toInt())
            rating.append("/")
            rating.append(5)
        }

        if(foodList[position].my_rating.toInt()==0){
            holder.itemView.tvRateFood.visibility=View.VISIBLE
        }else{
            holder.itemView.tvRateFood.visibility=View.GONE
        }
        holder.itemView.tvAverage.text=rating.toString()
        val tag= StringBuilder()
        foodList[position].tags.forEach {
            tag.append(it.name)
        }
        holder.itemView.tvDec.text=foodList[position].tagline


        var click = true
        holder.itemView.ingredientsBtn.setOnClickListener {
            if(click)
            {
                render.setAnimation(Fade().InDown(holder.itemView.tvCalenderExpandText))
                render.start()
                holder.itemView.tvCalenderExpandText.visibility = View.VISIBLE
                holder.itemView.ingredientsBtn.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_up, 0)
                click = false
            }
            else
            {
                render.setAnimation(Fade().OutUp(holder.itemView.tvCalenderExpandText))
                render.start()
                holder.itemView.ingredientsBtn.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_drop_down_white, 0)
                holder.itemView.tvCalenderExpandText.visibility = View.GONE
                click = true
            }
        }

        holder.itemView.tvRateFood.setOnClickListener {
            val dialog = context?.let { it1 -> Dialog(it1) }
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.alert_rate_the_food)

            val yesBtn = dialog?.findViewById(R.id.RatetheFoodSubmitBtn) as MaterialButton
            val closeBtn = dialog?.findViewById(R.id.ivRatetheFoodClose) as ImageView
            val ratingButton = dialog?.findViewById(R.id.rtRatetheFoodRatingBtn) as ScaleRatingBar
//            if(AppPreferences.chooseLanguage==Constants.ARABIC_LAG){
//                ratingButton.rotationY=-180f
//            }
            yesBtn.setOnClickListener {
                function.invoke(foodList[position].id,ratingButton.rating.toInt())
                dialog.dismiss()
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog?.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        holder.itemView.productImage.setOnClickListener {
            imageSliderList.clear()
            imageSliderList.add(foodList[position].image)
            StfalconImageViewer.Builder<String>(context, imageSliderList) { view, image ->
                view.background= ContextCompat.getDrawable(context,R.drawable.shape_placeholder)
                Glide.with(context)
                    .load(image)
                    .error(R.drawable.shape_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view)
            }.show()
        }
    }
}

package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.menu_day.Food
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.activity.InfoActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.item_meal.view.*

import render.animations.Fade
import render.animations.Render
import java.math.RoundingMode


class MealsAdapter(
    private val foodList: List<Food>,
    private val context: Context,
    private val function: (Int, Int) -> Unit,
    private val function2: () -> Unit
) : RecyclerView.Adapter<MealsAdapter.ViewHold>() {

    private var imageSliderList = ArrayList<String>()
    private var overlayView: PosterOverlayView? = null
    private lateinit var builder: StfalconImageViewer<String>

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val render = Render(context)

//        holder.itemView.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
//            0,
//            0,
//            R.drawable.ic_arrow_drop_down_white,
//            0
//        )
//        holder.itemView.meals_selectionBtn.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
        holder.itemView.tvFoodName.text = foodList[position].name
//        holder.itemView.ratingFood.rating =
//            foodList[position].average_rating.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
//        val rating = StringBuilder()
//        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
//            holder.itemView.llratingFood.rotationY = -360f
//            holder.itemView.tvAverage.rotationY = -360f
//            rating.append(5)
//            rating.append("/")
//            rating.append(
//                foodList[position].average_rating.toBigDecimal().setScale(1, RoundingMode.UP)
//                    .toFloat()
//            )
//        } else {
//            rating.append(
//                foodList[position].average_rating.toBigDecimal().setScale(1, RoundingMode.UP)
//                    .toFloat()
//            )
//            rating.append("/")
//            rating.append(5)
//        }
//        holder.itemView.tvAverage.text = rating.toString()

        holder.itemView.tvDescriptionContent.text = foodList[position].description
        val dec = StringBuilder()
//        dec.append(foodList[position].description)
//        dec.append("\n")
//        dec.append("\n")
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
        dec.append("\n")
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

        holder.itemView.tvTagExpand.text = dec.toString()
        var click = true
        if(foodList[position].ordered_status){
            holder.itemView.cardMain.strokeColor= Color.parseColor("#5DA7A3")
        }else{
            holder.itemView.cardMain.strokeColor= Color.parseColor("#00000000")
        }
//        holder.itemView.mealsSelectionRadioBtn.isChecked = foodList[position].ordered_status
//        holder.itemView.meals_selectionBtn.setOnClickListener {
//            if (click) {
//                render.setAnimation(Fade().InDown(holder.itemView.llExpand))
//                render.start()
//                holder.itemView.llExpand.visibility = View.VISIBLE
//                holder.itemView.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    R.drawable.ic_arrow_up,
//                    0
//                )
//                click = false
//            } else {
//                render.setAnimation(Fade().OutUp(holder.itemView.llExpand))
//                render.start()
//                holder.itemView.meals_selectionBtn.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    R.drawable.ic_arrow_drop_down_white,
//                    0
//                )
//                holder.itemView.llExpand.visibility = View.GONE
//                click = true
//            }
//        }
        CommonUtils.setImage(context, foodList[position].image, holder.itemView.ivFood)
//        holder.itemView.tvMacros.setOnClickListener {
//            context.startActivity(Intent(context,InfoActivity::class.java))
//        }

        holder.itemView.ivFood.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (AppPreferences.isPlan) {
                    function.invoke(foodList[position].id, position)
                } else {
                    function2.invoke()
                }

            } else {
                val intent = Intent(context, AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                context.startActivity(intent)
            }

        }

        holder.itemView.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (AppPreferences.isPlan) {
                    function.invoke(foodList[position].id, position)
                } else {
                    function2.invoke()
                }

            } else {
                val intent = Intent(context, AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                context.startActivity(intent)
            }
        }
        holder.itemView.setOnClickListener {
             function.invoke( foodList[position].id,position)
        }


    }

    private fun setOverlay() {
        overlayView = PosterOverlayView(context).apply {
            update()
            onClose = {
                builder.close()
            }
        }
    }
}

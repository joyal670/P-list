package com.proteinium.proteiniumdietapp.utils.calendar

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.proteinium.proteiniumdietapp.R


class EventDecorator(context: Activity?, dates: ArrayList<CalendarDay>, var calenderType: ArrayList<String>) : DayViewDecorator
{
    private val drawable: Drawable?
//    private val drawableResume: Drawable?
//    private val drawablePause: Drawable?
    private val dates: ArrayList<CalendarDay?>?
    private var position: Int? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates!!.contains(day)){
            position = dates.indexOf(day)
        }
        return dates!!.contains(day)
    }

    override fun decorate(view: DayViewFacade)
    {
//        calenderType.forEach {

//        if(position != null)
//        {
//            if(calenderType[position!!] == "resume")
//            {
//                view.setBackgroundDrawable(drawableResume!!)
//            }
//            else if(calenderType[position!!] == "complete")
//            {
//                view.setBackgroundDrawable(drawable!!)
//            }
//            else if(calenderType[position!!] == "pause")
//            {
//                view.setBackgroundDrawable(drawablePause!!)
//            }
//        }
        Log.e("TAG", "decorate: "+position )

//        }

        view.setBackgroundDrawable(drawable!!)
    }

    init {
        this.dates = ArrayList(dates!!)
        drawable = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_complete)
//        drawableResume = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_resume)
//        drawablePause = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_pause)
    }
}
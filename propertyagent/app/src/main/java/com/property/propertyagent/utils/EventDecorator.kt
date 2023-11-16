package com.property.propertyagent.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.property.propertyagent.R


class EventDecorator(context: Activity?, dates: ArrayList<CalendarDay>) : DayViewDecorator
{
    private val drawable: Drawable?
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
        view.setBackgroundDrawable(drawable!!)
    }

    init {
        this.dates = ArrayList(dates!!)
        drawable = ContextCompat.getDrawable(context!!, R.drawable.bg_date_single_event)

    }
}
package com.proteinium.proteiniumdietapp.utils.calendar

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.proteinium.proteiniumdietapp.R

class EventPauseDecorator(context: Activity?, dates: ArrayList<CalendarDay>, var calenderType: ArrayList<String>,var remoDate:String) :
    DayViewDecorator
{
    private val drawablePause: Drawable?
    private val dates: ArrayList<CalendarDay?>?
    private var position: Int? = null
    private var pos=false

    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates!!.contains(day)){
            position = dates.indexOf(day)
        }

        return dates!!.contains(day)
    }

    override fun decorate(view: DayViewFacade)
    {
        view.setBackgroundDrawable(drawablePause!!)
    }

    init {
        this.dates = ArrayList(dates!!)
        drawablePause = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_pause)
    }
}

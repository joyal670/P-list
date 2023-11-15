package com.proteinium.proteiniumdietapp.utils.calendar

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.proteinium.proteiniumdietapp.R

/**
 * Use a custom selector
 */
class HolidayDecorator(context: Activity?, offDays: ArrayList<CalendarDay>) :
    DayViewDecorator {

    private val drawable: Drawable?
    private val dates: ArrayList<CalendarDay?>?
    private var position: Int? = null


    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates!!.contains(day)){
            position = dates.indexOf(day)
        }
        return  dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setDaysDisabled(true)
        view.setBackgroundDrawable(drawable!!)
    }

    init {
        this.dates = ArrayList(offDays!!)
        drawable = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_off_days)
    }
}
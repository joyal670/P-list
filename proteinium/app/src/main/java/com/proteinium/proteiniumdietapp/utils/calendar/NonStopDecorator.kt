package com.proteinium.proteiniumdietapp.utils.calendar

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek
import java.util.*

/**
 * Use a custom selector
 */
class NonStopDecorator() : DayViewDecorator {


    override fun shouldDecorate(day: CalendarDay): Boolean {
        Log.e("7777", "shouldDecorate: "+day.day )
        Log.e("88888", "shouldDecorate: "+Calendar.FRIDAY )
        return day.date.dayOfWeek==DayOfWeek.FRIDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.setDaysDisabled(true)
        view.addSpan(StyleSpan(Typeface.NORMAL))
        view.addSpan(RelativeSizeSpan(1f))


    }


}
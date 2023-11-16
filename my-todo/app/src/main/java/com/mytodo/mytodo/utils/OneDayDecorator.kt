package com.mytodo.mytodo.utils

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.LocalDate

/**
 * Decorate a day by making the text big and bold
 */
class OneDayDecorator : DayViewDecorator {
    private var date: CalendarDay?
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return date != null && day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RelativeSizeSpan(1.0f))
    }

    /**
     * We're changing the internals, so make sure to call [MaterialCalendarView.invalidateDecorators]
     */
    fun setDate(date: LocalDate?) {
        this.date = CalendarDay.from(date)
    }

    init {
        date = CalendarDay.today()
    }
}
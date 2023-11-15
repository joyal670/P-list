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
class MySelectorDecorator(context: Activity?) : DayViewDecorator {
    private val drawable: Drawable

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
    }

    init {
        drawable = ContextCompat.getDrawable(context!!, R.drawable.date_selector)!!
    }
}
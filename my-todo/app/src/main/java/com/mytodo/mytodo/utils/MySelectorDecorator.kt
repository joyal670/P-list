package com.mytodo.mytodo.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mytodo.mytodo.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

/**
 * Use a custom selector
 */
class MySelectorDecorator(context: Activity?) : DayViewDecorator {
    private val drawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.date_selector)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
    }

}
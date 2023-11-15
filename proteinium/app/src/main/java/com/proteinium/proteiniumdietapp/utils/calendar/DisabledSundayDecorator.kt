package com.proteinium.proteiniumdietapp.utils.calendar

import android.app.Activity
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.proteinium.proteiniumdietapp.R
import org.threeten.bp.DayOfWeek

/**
 * Use a custom selector
 */
class DisabledSundayDecorator(context: Activity?, offDays: ArrayList<CalendarDay>) :
    DayViewDecorator {

    private val drawable: Drawable?
    private val dates: ArrayList<CalendarDay?>?
    private var position: Int? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates!!.contains(day)){
            position = dates.indexOf(day)
        }
        return day.date.dayOfWeek == DayOfWeek.SUNDAY  && dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setDaysDisabled(true)
        view.addSpan(StyleSpan(Typeface.NORMAL))
        view.addSpan(RelativeSizeSpan(1f))
        //view.setBackgroundDrawable(drawable!!)
    }

    init {
        this.dates = ArrayList(offDays!!)
        drawable = ContextCompat.getDrawable(context!!, R.drawable.bg_date_round_border_off_days)
    }

}
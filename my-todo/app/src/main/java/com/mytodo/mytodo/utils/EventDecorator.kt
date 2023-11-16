package com.mytodo.mytodo.utils

import android.app.Activity
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import androidx.core.content.ContextCompat
import com.mytodo.mytodo.R
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import io.realm.RealmResults


class EventDecorator(private val context: Activity?, dates: ArrayList<CalendarDay>) : DayViewDecorator
{
    private val drawable: Drawable?
    private val dates: ArrayList<CalendarDay?>?
    private var position: Int? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates!!.contains(day)){
            position = dates.indexOf(day)
        }
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade)
    {
        view.setBackgroundDrawable(drawable!!)
        view.addSpan(TextAppearanceSpan(context, R.style.CustomTextAppearance1))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RelativeSizeSpan(1.0f))

    }

    init {
        this.dates = ArrayList(dates)
        drawable = ContextCompat.getDrawable(context!!, R.drawable.sample)

    }
}
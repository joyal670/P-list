package com.iroid.jeetmeet.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class RemoveDecorator(dates: Collection<CalendarDay>?) :
    DayViewDecorator {
    private val dates: HashSet<CalendarDay>

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
        // also tried with just
        // return false;
    }

    override fun decorate(view: DayViewFacade) {
        // TODO: what to do?
    }

    init {
        this.dates = HashSet(dates)
    }
}
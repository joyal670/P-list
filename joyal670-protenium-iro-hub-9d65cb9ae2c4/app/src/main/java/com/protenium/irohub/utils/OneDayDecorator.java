package com.protenium.irohub.utils;

import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.jetbrains.annotations.Nullable;
import org.threeten.bp.LocalDate;

public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date = CalendarDay.today();

    public OneDayDecorator() {
        this.date = CalendarDay.today();
    }

    public final void setDate(@Nullable LocalDate date) {
        this.date = CalendarDay.from(date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day == date;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.0f));
    }


}

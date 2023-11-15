package com.protenium.irohub.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.protenium.irohub.R;

import java.util.ArrayList;

public class ResumeDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private ArrayList<CalendarDay> date;
    private int position;

    public ResumeDecorator(Activity activity, ArrayList<CalendarDay> dates) {
        drawable = ContextCompat.getDrawable(activity, R.drawable.bg_date_round_border_resume);
        date = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(date.contains(day)){
            position = date.indexOf(day);
        }
        return date.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);

    }
}

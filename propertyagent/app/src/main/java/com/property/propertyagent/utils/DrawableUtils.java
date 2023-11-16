package com.property.propertyagent.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.content.ContextCompat;


import com.property.propertyagent.R;




public final class DrawableUtils {

    /*public static Drawable getCircleDrawableWithText(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.sample_circle);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }*/

    public static Drawable getThreeDots(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_three_icons);

        //Add padding to too large icon
        return new InsetDrawable(drawable, 300, 0, 300, 0);
    }

    public static Drawable getTwoDots(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_two_icons);

        //Add padding to too large icon
        return new InsetDrawable(drawable, 400, 0, 400, 0);
    }

    public static Drawable getOneDots(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_one_icons);

        //Add padding to too large icon
        return new InsetDrawable(drawable, 500, 0, 500, 0);
    }

    private DrawableUtils() {
    }
}

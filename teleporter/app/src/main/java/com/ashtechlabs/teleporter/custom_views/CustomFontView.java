package com.ashtechlabs.teleporter.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontView extends TextView {

	public CustomFontView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setFont();
	}

	public CustomFontView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFont();
	}

	public CustomFontView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setFont();
	}
	
	void setFont()
	{
		//this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-B.ttf"));
	}

}

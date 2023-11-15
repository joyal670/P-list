package com.mytodo.mytodo.listeners

import android.graphics.fonts.FontFamily

interface FragmentTransInterface {
    fun setTitleFromFragment(title: String)
    fun setTitleinCenter(center : Boolean)
    fun setTitleCaptial(cap : Boolean)
    fun setFontFamily(fontFamily: Int)
}
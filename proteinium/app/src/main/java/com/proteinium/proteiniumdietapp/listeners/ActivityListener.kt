package com.proteinium.proteiniumdietapp.listeners

import androidx.fragment.app.Fragment

interface ActivityListener {
    fun navigateToFragment(fragment: Fragment)
    fun setTitle(title:String, size : Float, fontFamily: Int, textAllCaps : Boolean)
    fun setBackButton(backEnabled : Boolean)
    fun hideToolbar(hideToolbar : Boolean)
}
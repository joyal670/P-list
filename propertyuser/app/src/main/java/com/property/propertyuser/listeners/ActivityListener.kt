package com.property.propertyuser.listeners

import androidx.fragment.app.Fragment

interface ActivityListener {
    fun navigateToFragment(fragment: Fragment)
    fun setTitle(title:String)
}
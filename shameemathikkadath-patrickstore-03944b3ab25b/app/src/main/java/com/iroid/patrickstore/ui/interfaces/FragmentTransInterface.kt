package com.iroid.patrickstore.ui.interfaces

import androidx.fragment.app.Fragment

interface FragmentTransInterface  {
    fun replaceFragment(fragment: Fragment)
    fun replaceFragment(fragmentTag: String)
    fun popFragment()
    fun popReplaceFragment(fragment: Fragment)
    fun setTitleFromFragment(title: String)
}
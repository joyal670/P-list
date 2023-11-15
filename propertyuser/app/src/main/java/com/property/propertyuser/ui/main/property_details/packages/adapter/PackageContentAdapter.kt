package com.property.propertyuser.ui.main.property_details.packages.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.ui.main.property_details.packages.fragments.WeeklyFragment

class PackageContentAdapter(
    fm: FragmentManager?,
    lifecycle: Lifecycle,
    private  val function: (String) -> Unit
) : FragmentStateAdapter(fm!!, lifecycle) {

    private val items: Int = 7

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =
                WeeklyFragment { id ->
                    function.invoke(id)
                }
            1 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
            2 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
            3 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
            4 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
            5 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
            6 -> fragment =
                WeeklyFragment {
                    function.invoke(it)
                }
        }
        return fragment!!
    }
    override fun getItemCount(): Int {
        return items
    }
}
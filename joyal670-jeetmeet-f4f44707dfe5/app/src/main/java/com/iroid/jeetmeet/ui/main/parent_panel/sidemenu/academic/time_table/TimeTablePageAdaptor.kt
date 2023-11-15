package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TimeTablePageAdaptor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment>

    init {
        fragments = ArrayList()
    }

    /* override fun getPageTitle(position: Int): CharSequence? {
         return super.getPageTitle(position)
     }
 */
    fun addFragment(
        fragment: Fragment,
    ) = fragments.add(fragment)

    /* override fun getCount(): Int {
         return fragments.size
     }

     override fun getItem(position: Int): Fragment {
         return fragments.get(position)
     }*/

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position)
    }

}
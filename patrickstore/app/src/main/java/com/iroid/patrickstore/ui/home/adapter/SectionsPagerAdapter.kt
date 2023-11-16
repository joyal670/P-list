package com.iroid.patrickstore.ui.home.adapter

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.home.ProductCategory
import com.iroid.patrickstore.model.home.Shop
import com.iroid.patrickstore.ui.home.fragment.FoodFragment
import com.iroid.patrickstore.ui.home.fragment.ShppingFragment
import com.iroid.patrickstore.ui.home.fragment.ShopFragment

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager,private val titleList: List<String>)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ShppingFragment (defined as a static inner class below).
        return ShppingFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return titleList.size
    }
}
class ShopPagerAdapterShop(fragmentManager: FragmentManager,
                           lifecycle: Lifecycle, private val shopList :ArrayList<Fragment>)
    : FragmentStateAdapter(fragmentManager,lifecycle) {


    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun createFragment(position: Int): Fragment {
        return shopList[position]
    }
    fun getViewAtPosition(position: Int): View? {
        return shopList[position].view
    }
}
class PagerAdapterFood(fragmentManager: FragmentManager,
                       lifecycle: Lifecycle, private val productList :ArrayList<Fragment>)
    : FragmentStateAdapter(fragmentManager,lifecycle)  {
    override fun getItemCount(): Int {
        return productList.size

    }

    override fun createFragment(position: Int): Fragment {
        return productList[position]
    }
    fun getViewAtPosition(position: Int): View? {
        return productList[position].view
    }


}
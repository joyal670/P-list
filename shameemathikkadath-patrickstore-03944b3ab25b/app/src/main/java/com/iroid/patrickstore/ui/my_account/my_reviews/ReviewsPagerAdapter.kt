package com.iroid.patrickstore.ui.my_account.my_reviews

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderItem


class ReviewsPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val items: List<DeliveredOrderItem>
):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ReviewsFragment.newInstance(items)
            else -> UnReviewsFragment.newInstance(items)
        }
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}

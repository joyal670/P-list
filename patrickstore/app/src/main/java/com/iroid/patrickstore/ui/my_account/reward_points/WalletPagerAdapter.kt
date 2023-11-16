package com.iroid.patrickstore.ui.my_account.reward_points

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.iroid.patrickstore.ui.my_account.reward_points.cash_back.CashBackFragment

class WalletPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val totalRewardPoints: Int
):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RewardPointsFragment.newInstance(totalRewardPoints)
            else -> CashBackFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}

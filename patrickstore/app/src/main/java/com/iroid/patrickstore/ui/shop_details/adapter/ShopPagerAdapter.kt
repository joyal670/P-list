package com.iroid.patrickstore.ui.shop_details.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.iroid.patrickstore.model.seller.SellingCategory
import com.iroid.patrickstore.model.service.service_list.Item
import com.iroid.patrickstore.ui.shop_details.about.ShopAboutFragment
import com.iroid.patrickstore.ui.shop_details.products.ShopProductFragment
import com.iroid.patrickstore.ui.shop_details.reviews.ShopReviewsFragment
import com.iroid.patrickstore.ui.shop_details.services.ShopServicesFragment

class ShopPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val sellingCategory: List<SellingCategory>,
    private val bundle: Bundle,
    private val storeId: String,
    private val items: List<Item>
): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ShopProductFragment.newInstance(sellingCategory)
            1 -> ShopServicesFragment.newInstance(items)
            2 -> ShopAboutFragment.newInstance(bundle)
            else -> ShopReviewsFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return titleList.size
    }

}

package com.iroid.patrickstore.ui.productdetail.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.productdetail.fragment.ProductDetailFragment
import com.iroid.patrickstore.ui.productdetail.fragment.ReviewFragment
import com.iroid.patrickstore.utils.Constants

class ProductPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val description: String,
    private val similarProducts: List<Product>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                fragmentManager.setFragmentResult(Constants.REQUEST_KEY_SIMILAR, bundleOf(Constants.BUNDLE_KEY_SIMILAR to similarProducts))
                ProductDetailFragment.newInstance(description)
            }

            1 ->{
                ReviewFragment.newInstance(similarProducts)
            }
            else -> ProductDetailFragment.newInstance(description)
        }

    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}
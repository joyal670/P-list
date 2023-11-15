package com.iroid.patrickstore.ui.shop_details.about


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentShopAboutBinding
import com.iroid.patrickstore.ui.shop_details.ShopDetailsViewModal
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Constants.BUNDLE_DEC
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_EMAIL
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_LOCATION
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_PHONE


class ShopAboutFragment : BaseFragment<ShopDetailsViewModal,FragmentShopAboutBinding>() {

    companion object{
        fun newInstance(bundle: Bundle) = ShopAboutFragment().apply {
            arguments = bundleOf()
            arguments!!.putBundle(Constants.BUNDLE_ABOUT,bundle)
        }
    }

    override fun initViews() {
        val bundle:Bundle= arguments!!.getBundle(Constants.BUNDLE_ABOUT)!!
        binding.textView8.text= bundle[BUNDLE_KEY_LOCATION].toString()
        binding.tvMailText.text= bundle[BUNDLE_KEY_EMAIL].toString()
        binding.tvPhone.text= bundle[BUNDLE_KEY_PHONE].toString()
        binding.tvShopDescription.text= bundle[BUNDLE_DEC].toString()
    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentShopAboutBinding {
       return FragmentShopAboutBinding.inflate(layoutInflater)
    }


}

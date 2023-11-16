package com.iroid.patrickstore.ui.shop_details.services


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentShopServicesBinding
import com.iroid.patrickstore.model.service.service_list.Item
import com.iroid.patrickstore.ui.shop_details.adapter.ShopServiceAdapter
import com.iroid.patrickstore.ui.shop_details.services.service_details.ServiceDetailViewModal
import com.iroid.patrickstore.ui.shop_details.services.service_details.ServiceDetailsActivity
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.fragment_shop_services.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ShopServicesFragment : BaseFragment<ServiceDetailViewModal, FragmentShopServicesBinding>() {

    companion object {
        fun newInstance(serviceList: List<Item>) = ShopServicesFragment().apply {
            arguments = bundleOf()
            arguments!!.putParcelableArrayList(Constants.ARGUMENT_SERVICE,serviceList as ArrayList<out Parcelable>)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireArguments().getParcelableArrayList<Item>(Constants.ARGUMENT_SERVICE).let { serviceList ->
            setUpServiceAdapter(serviceList!!)
        }
    }

    override fun initViews() {

        }


    override fun setUpObserver() {

    }

    private fun setUpServiceAdapter(serviceList: List<Item>) {
        rvShopServices.layoutManager = LinearLayoutManager(context)
        rvShopServices.adapter =
            activity?.let { ShopServiceAdapter(it,serviceList) { it ->

            }
            }
    }


    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentShopServicesBinding {
        return FragmentShopServicesBinding.inflate(layoutInflater)
    }


}

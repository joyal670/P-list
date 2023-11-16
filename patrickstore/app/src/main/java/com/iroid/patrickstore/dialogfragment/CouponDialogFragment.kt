package com.iroid.patrickstore.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.databinding.DialogCouponBinding
import com.iroid.patrickstore.model.coupon.ItemCoupon
import com.iroid.patrickstore.ui.coupon.adapter.CouponAdapter
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Constants.REQUEST_KEY_CODE
import com.iroid.patrickstore.utils.Constants.REQUEST_KEY_COUPON

class CouponDialogFragment : DialogFragment() {
    private var couponList:List<ItemCoupon> =ArrayList<ItemCoupon>()
    private lateinit var binding: DialogCouponBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCouponBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(REQUEST_KEY_COUPON){
                requestKey, bundle ->
            val couponList:List<ItemCoupon> = bundle.getParcelableArrayList<ItemCoupon>(Constants.BUNDLE_KEY_COUPON)!!
            val couponAdapter = CouponAdapter(requireContext(),couponList) {coupon_code->
                setFragmentResult(REQUEST_KEY_CODE, bundleOf(Constants.BUNDLE_KEY_CODE to coupon_code) )
                dismiss()

            }
            binding.rvCoupon.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvCoupon.adapter = couponAdapter

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }




}
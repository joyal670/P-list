package com.iroid.patrickstore.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.iroid.patrickstore.databinding.DialogInfoBinding

class InfoDialogFragment : DialogFragment() {
    private lateinit var binding: DialogInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInfoBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFragmentResultListener(REQUEST_KEY_COUPON){
//                requestKey, bundle ->
//            val couponList:List<ItemCoupon> = bundle.getParcelableArrayList<ItemCoupon>(Constants.BUNDLE_KEY_COUPON)!!
//            val couponAdapter = CouponAdapter(requireContext(),couponList) {coupon_code->
//                setFragmentResult(REQUEST_KEY_CODE, bundleOf(Constants.BUNDLE_KEY_CODE to coupon_code) )
//                dismiss()
//
//            }
//
//
//        }

    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return dialog
    }




}

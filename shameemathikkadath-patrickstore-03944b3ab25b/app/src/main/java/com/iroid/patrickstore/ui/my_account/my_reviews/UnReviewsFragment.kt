package com.iroid.patrickstore.ui.my_account.my_reviews


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager

import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderItem
import com.iroid.patrickstore.model.delivered_order.Product
import com.iroid.patrickstore.ui.my_account.my_reviews.adapter.ReviewedListAdapter
import com.iroid.patrickstore.ui.my_account.my_reviews.adapter.UnReviewedListAdapter
import kotlinx.android.synthetic.main.fragment_my_reviews.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class UnReviewsFragment : Fragment() {


    companion object{
        fun newInstance(items: List<DeliveredOrderItem>) = UnReviewsFragment().apply {
            arguments = bundleOf()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listProduct: ArrayList<Product> = ArrayList<Product>()
        rvUnReviewed.layoutManager = LinearLayoutManager(context)
        rvUnReviewed.adapter = UnReviewedListAdapter(
            requireContext(),
            listProduct
        ) {
            showReviewDialog()
        }
//        rvReviewed.layoutManager = LinearLayoutManager(context)
//        rvReviewed.adapter = ReviewedListAdapter(it.data.data.items) {
//
//        }


    }
    private fun showReviewDialog() {
            val dialog = Dialog(requireContext())
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_wrirte_review)


            //val yesBtn = dialog?.findViewById(R.id.RatetheFoodSubmitBtn) as MaterialButton
//        val closeBtn = dialog.findViewById(R.id.ivClose) as ImageView



            dialog.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


}

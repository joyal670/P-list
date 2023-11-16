package com.iroid.patrickstore.ui.my_account.my_reviews


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentMyReviewsBinding
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderItem
import com.iroid.patrickstore.model.delivered_order.Product
import com.iroid.patrickstore.ui.my_account.my_reviews.adapter.ReviewedListAdapter
import com.iroid.patrickstore.ui.my_account.my_reviews.adapter.UnReviewedListAdapter
import com.iroid.patrickstore.ui.my_account.reward_points.WalletViewModal
import com.iroid.patrickstore.utils.Constants.ARG_DELIVERED_ORDER
import com.iroid.patrickstore.utils.Status
import com.iroid.patrickstore.utils.Toaster
import com.willy.ratingbar.ScaleRatingBar
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.fragment_my_reviews.*
import kotlinx.android.synthetic.main.fragment_my_reviews.noData
import kotlinx.android.synthetic.main.layout_no_data.view.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewsFragment : BaseFragment<WalletViewModal, FragmentMyReviewsBinding>() {
    private lateinit var dialog: Dialog

    companion object {
        fun newInstance(items: List<DeliveredOrderItem>) = ReviewsFragment().apply {
            arguments = bundleOf()
            arguments!!.putParcelableArrayList(
                ARG_DELIVERED_ORDER,
                items as ArrayList<out Parcelable>
            )
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listProduct: ArrayList<Product> = ArrayList<Product>()
        requireArguments().getParcelableArrayList<DeliveredOrderItem>(ARG_DELIVERED_ORDER)!!
            .forEach {
                it.cartItems.forEach { cartItem ->
                    listProduct.add(cartItem.product)
                }
            }

        if(listProduct.isNotEmpty()){
            rvUnReviewed.layoutManager = LinearLayoutManager(context)
            rvUnReviewed.adapter = UnReviewedListAdapter(requireContext(), listProduct) {
                showReviewDialog(it, "", 0f, "",
                    "", "Add")
            }
            constraintMain.visibility= View.VISIBLE
            noData.visibility=View.GONE


        }else{
            constraintMain.visibility= View.GONE
            noData.visibility=View.VISIBLE
            noData.noDataLottie.setAnimation(R.raw.order_now_animation)
            noData.noDataLottie.loop(true)
            noData.noDataLottie.playAnimation()
            noData.tvNoData.text="Empty delivered order"
        }

//        rvReviewed.layoutManager = LinearLayoutManager(context)
//        rvReviewed.adapter = ReviewedListAdapter(it.data.data.items, {
//
//        },requireContext())
        viewModel.getProductReview()
    }

    private fun showReviewDialog(
        productId: String,
        reviewId: String,
        rating: Float,
        title: String,
        review: String,
        type: String
    ) {
        dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_wrirte_review)

        val simpleRatingBar: ScaleRatingBar =
            dialog?.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
        val etTitle: EditText = dialog?.findViewById(R.id.etTitle) as EditText
        val etReview: EditText = dialog?.findViewById(R.id.etReview) as EditText
        val btnSubmit: Button = dialog?.findViewById(R.id.btnSubmit) as Button

        if(type=="Edit"){
            etTitle.setText(title)
            etReview.setText(review)
            simpleRatingBar.rating=rating
        }
        btnSubmit.setOnClickListener {
            if(type=="Edit"){
                callEditReviewApi(
                    reviewId,
                    simpleRatingBar.rating,
                    etTitle.text.toString(),
                    etReview.text.toString()
                )
            }else{
                callAddReviewApi(
                    productId,
                    simpleRatingBar.rating,
                    etTitle.text.toString(),
                    etReview.text.toString()
                )
            }
        }
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

    private fun callEditReviewApi(reviewId: String, rating: Float, title: String,review:String) {
        viewModel.updateReview(reviewId,rating,title,review)
    }

    private fun callAddReviewApi(
        productId: String,
        review: Float,
        title: String,
        comments: String
    ) {
        viewModel.addReview(title, productId, review, comments)
    }

    override fun initViews() {

    }

    override fun setUpObserver() {
        viewModel.addReviewLiveData.observe(this, Observer {

            when (it.status!!) {
                Status.SUCCESS -> {
                    dialog.dismiss()
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.msg,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                }
                Status.ERROR -> {
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                    dismissProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
            }
        })
        viewModel.productReviewLiveData.observe(this, Observer {

            when (it.status!!) {
                Status.SUCCESS -> {
                    rvReviewed.layoutManager = LinearLayoutManager(context)
                    rvReviewed.adapter = ReviewedListAdapter(
                        it.data!!.data.items,
                        {deleteId->
                            viewModel.deleteReview(deleteId)
                        },
                        requireContext()
                    ) { reviewId, rating, title, review ->
                       showReviewDialog("",reviewId,rating,title,review,"Edit")
                    }
                }
                Status.ERROR -> {
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
            }
        })

        viewModel.updateReviewLiveData.observe(this, Observer {

            when (it.status!!) {
                Status.SUCCESS -> {
                    dialog.dismiss()

                    Toaster.showToast(
                        requireContext(),
                        it.data!!.msg,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                }
                Status.ERROR -> {
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
            }
        })
    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentMyReviewsBinding {
        return FragmentMyReviewsBinding.inflate(layoutInflater)
    }
}




package com.iroid.emergency.main.settings.feedback

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentFeedbackBinding
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster

class FeedbackFragment : BaseFragment<SettingsViewModal, FragmentFeedbackBinding>() {
    lateinit var bottom: BottomSheetDialog
    override fun initViews() {
        bottom = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        binding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                showFeedbackBottom()
            }
    }

    override fun setUpObserver() {
        viewModel.feedbackLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    Toaster.showToast(requireContext(),it.data!!.message,Toaster.State.SUCCESS,Toast.LENGTH_LONG)
                }
                Status.ERROR->{
                    Toaster.showToast(requireContext(),it.message!!,Toaster.State.ERROR,Toast.LENGTH_LONG)
                    dismissProgress()
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                }
            }
        })
    }

    private fun showFeedbackBottom() {
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.bottom_feedback, null)
        bottom.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottom.setContentView(bottomSheet)
        val btnFeedback=bottomSheet.findViewById<Button>(R.id.btnFeedback)
        val etComments=bottomSheet.findViewById<EditText>(R.id.etComments)
        btnFeedback.setOnClickListener {
            bottom.dismiss()
            viewModel.feedbackUpdate(binding.ratingBar.rating,etComments.text.toString())
        }
        bottom.show()

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentFeedbackBinding {
        return FragmentFeedbackBinding.inflate(layoutInflater)
    }
}

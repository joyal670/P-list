package com.iroid.emergency.main.settings.submission

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentSubmitDetailsBinding
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.preference.ConstantPreference
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster

class SubmissionFragment : BaseFragment<SettingsViewModal, FragmentSubmitDetailsBinding>() {
    private var type =""
    override fun initViews() {
        binding.etName.setText(AppPreferences.userName!!)
        binding.etMobile.setText(
            AppPreferences.userMobile
        )
        binding.radioType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                type = if(checkedId==R.id.radio_pirates){
                    ConstantPreference.PRIMARY_TYPE
                }else{
                    ConstantPreference.SECONDARY_TYPE
                }
            }

        })
    }

    override fun setUpObserver() {
        viewModel.submissionLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    showSosDialog(it.data!!.message)
                }
                Status.ERROR->{
                    Toaster.showToast(requireContext(), it.message!!, Toaster.State.ERROR)
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

    override fun setOnClick() {
        binding.btnSignUp.setOnClickListener {
            if(binding.etName.text.toString().isNotEmpty() && binding.etMobile.text.isNotEmpty() && !TextUtils.isEmpty(type)){
                viewModel.updateSubmission(
                    binding.etName.text.toString(),
                    binding.etMobile.text.toString(),
                    type
                )
            } else {
                Toaster.showToast(requireContext(), getString(R.string.all_fields_are_required), Toaster.State.WARNING)
            }

        }
    }

    override fun getViewBinding(): FragmentSubmitDetailsBinding {
        return FragmentSubmitDetailsBinding.inflate(layoutInflater)
    }

    private fun showSosDialog(message: String) {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_submission)
        dialog.show()
        val btnDone=dialog.findViewById<Button>(R.id.btnDone)
        val tvDec=dialog.findViewById<TextView>(R.id.tvDec)
        tvDec.text=message
        btnDone.setOnClickListener {
            dialog.dismiss()
            requireActivity().finish()
        }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

package com.iroid.emergency.start_up.auth.sign_up

import android.app.Dialog
import android.content.Context.WINDOW_SERVICE
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentSignupBinding
import com.iroid.emergency.main.settings.SettingsActivity
import com.iroid.emergency.preference.ConstantPreference
import com.iroid.emergency.start_up.auth.AuthViewModal
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import com.iroid.emergency.start_up.utils.isConnected
import com.iroid.emergency.start_up.utils.nextActivity


class SignUpFragment : BaseFragment<AuthViewModal, FragmentSignupBinding>() {
    override fun initViews() {
        showInfoDialog()
    }

    override fun setUpObserver() {
        viewModel.signUpLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    val bundle = bundleOf("mobile" to binding.etMobile.text.toString().trim())
                    findNavController().navigate(R.id.action_signUpFragment_to_otpFragment2,bundle,  NavOptions.Builder().setLaunchSingleTop(true).build())
                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(requireContext(),it.message!!,Toaster.State.ERROR)
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                    if(requireActivity().isConnected){
                        Toaster.showToast(requireContext(),"Something went wrong",Toaster.State.ERROR)
                    }else{
                        Toaster.showToast(requireContext(),"Please check internet",Toaster.State.ERROR)
                    }
                }

            }
        })
    }

    private fun showInfoDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_primary)
        dialog.show()

        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)

        ivClose.setOnClickListener {
            dialog.dismiss()

        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setOnClick() {
        binding.btnSignUp.setOnClickListener {
            if(binding.etName.text.toString().isNotEmpty() && binding.etMobile.text.toString().isNotEmpty()){
                viewModel.register(binding.etName.text.toString(),binding.etMobile.text.toString())
            } else {
                Toaster.showToast(requireContext(), getString(R.string.all_fields_are_required), Toaster.State.WARNING)
            }
        }
        binding.btnCareGiver.setOnClickListener {
            val bundle= Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.SUBMISSION)
            requireActivity().nextActivity(SettingsActivity::class.java, bundle)
        }
    }

    override fun getViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }
}

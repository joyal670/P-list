package com.iroid.emergency.main.settings.profile

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.awesomedialog.*
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentEditProfileBinding
import com.iroid.emergency.databinding.FragmentViewProfileBinding
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BaseFragment<SettingsViewModal, FragmentEditProfileBinding>() {
    override fun initViews() {
        viewModel.getProfile()
    }

    override fun setUpObserver() {
        viewModel.profileLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    binding.etName.setText(it.data!!.user.name)
                    binding.etMobile.setText(it.data!!.user.mobile.toString())
                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(requireContext(),it.message!!, Toaster.State.ERROR)
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.NO_INTERNET->{
                    dismissProgress()

                }
            }
        })

        viewModel.updateLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    if(it.data!!.otp_status){
                        findNavController().navigate(R.id.action_editProfileFragment_to_otpFragmentProfile)
                    }else{
                        showProfileUpdate(it.data!!.message)
                    }


                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(requireContext(),it.message!!, Toaster.State.ERROR)
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
            AppPreferences.userName=binding.etName.text.toString()
            AppPreferences.userMobile=binding.etMobile.text.toString()
            viewModel.updateProfile(etName.text.toString().trim(),etMobile.text.toString().trim())
        }
    }

    override fun getViewBinding(): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(layoutInflater)
    }
    private fun showProfileUpdate(message: String) {
        AwesomeDialog.build(requireActivity())
            .title("Success")
            .body(message)
            .icon(R.drawable.ic_icon2)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                requireActivity().onBackPressed()
            }

    }
}

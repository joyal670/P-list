package com.property.propertyuser.ui.startup.auth.otp

//import com.property.propertyuser.data.api.ApiHelperImpl
//import com.property.propertyuser.data.api.RetrofitBuilder
/*import com.property.propertyuser.utils.ViewModelFactory*/
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentOtpBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.utils.Constants.ARG_MOBILE
import com.property.propertyuser.utils.Constants.ARG_OTP
import com.property.propertyuser.utils.GenericTextWatcher
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_otp.*

class OtpFragment : BaseFragment() {

    private lateinit var activityListener: ActivityListener
    private lateinit var otpViewModel: OtpViewModel
    private lateinit var binding: FragmentOtpBinding
    private lateinit var otp: String
    private lateinit var mobile: String
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(otp: String, phone: String) = OtpFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_OTP, otp)
                putString(ARG_MOBILE, phone)
            }
        }

    }

    override fun initData() {
        otp = arguments?.get(ARG_OTP).toString()
        mobile = arguments?.get(ARG_MOBILE).toString()
        tvVerificationText.text = getString(R.string.tvVerificationText) + " " + mobile
        setupOtp()
    }

    private fun setupOtp() {
        otp_box1.setText(otp[0].toString())
        otp_box2.setText(otp[1].toString())
        otp_box3.setText(otp[2].toString())
        otp_box4.setText(otp[3].toString())
        val edit = arrayOf<EditText>(otp_box1, otp_box2, otp_box3, otp_box4)
        otp_box1.addTextChangedListener(GenericTextWatcher(otp_box1, edit))
        otp_box2.addTextChangedListener(GenericTextWatcher(otp_box2, edit))
        otp_box3.addTextChangedListener(GenericTextWatcher(otp_box3, edit))
        otp_box4.addTextChangedListener(GenericTextWatcher(otp_box4, edit))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as OtpActivity

    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.title_otp))
    }

    override fun setupViewModel() {
        otpViewModel = OtpViewModel()
    }

    override fun setupObserver() {
        otpViewModel.getOtpLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    AppPreferences.isLogin = true
                    AppPreferences.token = it.data!!.data.api_token
                    if (it.data.data.name.isNotBlank()) {
                        AppPreferences.user_name = it.data.data.name
                    }
                    if (it.data.data.email.isNotBlank()) {
                        AppPreferences.user_email = it.data.data.email
                    }
                    if (it.data.data.profile_pic.isNotBlank()) {
                        AppPreferences.user_profile_image = it.data.data.profile_pic
                    }
                    startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                    requireActivity().finish()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.something_wrong),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.no_internet),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })

        otpViewModel.getResendOtpResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data != null) {
                        Toaster.showToast(
                            requireContext(),
                            it.data.response,
                            Toaster.State.SUCCESS,
                            Toast.LENGTH_LONG
                        )
                        otp = it.data.otp.toString()
                        setupOtp()
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.something_wrong),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.no_internet),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {
        binding.btnVerify.setOnClickListener {
            if (!otp_box1.text.toString().trim().isNullOrEmpty() &&
                !otp_box2.text.toString().trim().isNullOrEmpty() &&
                !otp_box3.text.toString().trim().isNullOrEmpty() &&
                !otp_box4.text.toString().trim().isNullOrEmpty()
            ) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        otpViewModel.otpVerify(mobile, otp, task.result)
                    }

                }

            } else {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.otp_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
        }
        tvResendCode.setOnClickListener {
            otpViewModel.reSendOtp(mobile)
        }
    }
}
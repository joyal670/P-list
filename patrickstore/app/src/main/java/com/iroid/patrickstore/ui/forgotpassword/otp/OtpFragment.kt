package com.iroid.patrickstore.ui.forgotpassword.otp

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.ActivityOtpBinding
import com.iroid.patrickstore.utils.*


class OtpFragment : BaseFragment<OtpViewModel, ActivityOtpBinding>() {

    private lateinit var optNumber: String
    private val optArgs: OtpFragmentArgs by navArgs()

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_otp, container, false)
        root.findViewById<Button>(R.id.btnVerify)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_otp, null)
        )
        return root
    }*/

    override fun initViews() {
        optNumber = optArgs.otp
        binding.otpView.otp = optNumber
    }

    override fun setUpObserver() {
        viewModel.otpLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val action = OtpFragmentDirections.actionOtp()
                    NavHostFragment.findNavController(this).navigate(action)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        })
    }


    override fun setOnClick() {
        binding.btnVerify.setOnClickListener {
            // viewModel.otpVerify(binding.otpView.otp)
            val action = OtpFragmentDirections.actionOtp()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun getViewBinding(): ActivityOtpBinding {
        return ActivityOtpBinding.inflate(layoutInflater)
    }

}
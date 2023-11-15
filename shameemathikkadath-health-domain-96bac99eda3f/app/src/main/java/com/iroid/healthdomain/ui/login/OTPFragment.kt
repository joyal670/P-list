package com.iroid.healthdomain.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.iroid.healthdomain.data.dummyModel.PhoneModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.FragmentOtpBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.HomeActivity
import com.iroid.healthdomain.ui.home.fit.FitRequestCode
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.profile_set_up.InitialProfileSetupActivity
import com.iroid.healthdomain.ui.utils.enable
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.ui.utils.showSnackBar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class OTPFragment : BaseFragment<LoginViewModel, FragmentOtpBinding, LoginRepository>() {
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
        .build()
    val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private lateinit var number: String
    val args: OTPFragmentArgs by navArgs()

    companion object {
        private const val TAG = "OTPFragment"
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOtpBinding {
        return FragmentOtpBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getFragmentRepository(): LoginRepository {
        return LoginRepository(
                api = remoteDataSource.buildApi(ApiServices::class.java),
                preferences = userPreferences)
    }


    var countries = arrayOf("A", "B", "C")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        number = args.phoneNumber
        binding.btVerify.enable(false)

        val phoneModel: PhoneModel = PhoneModel(number = " +91 $number")
        binding.model = phoneModel

        userPreferences.number.asLiveData().observe(viewLifecycleOwner, Observer {
            print("Phonne $it")
        })

        binding.otpView.doOnTextChanged { text, start, before, count ->
            if (text.toString().length == 4)
                binding.btVerify.enable(true)
            else binding.btVerify.enable(false)
        }

        binding.btVerify.setOnClickListener {
            var otpValue: String = binding.otpView.text.toString()
            validate(otpValue, number)
        }

        binding.tvResend.setOnClickListener {
            viewModel.reSendOtp(number)
        }

        addObserver()
    }

    private fun addObserver() {

        viewModel.validateOTPResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {
                    viewModel.setLoading(false)

                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.data.token)
                        viewModel.savePhoneNumber(number)

                        val created: String = it.value.data.user_details.created_at
                        val updated: String = it.value.data.user_details.updated_at



                        if (it.value.data.user_details.name!=null) {
                            AppPreferences.isLogin=true
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)

                        } else {
                            val intent = Intent(activity, InitialProfileSetupActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                    }


                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)

                    handleApiError(requireView(), it)
                }
            }
        })

        viewModel.reSendOTPResponse.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (it.value.status == 200)
                        requireView().showSnackBar("OTP resend Successfully")
                }
                is Resource.Failure -> {
                    requireView().showSnackBar("OTP resend failed")
                    //handleApiError(requireView(), it)
                }
            }


        })
    }


    private fun validate(otpValue: String, number: String) {
        if (otpValue.equals(null) || otpValue.isEmpty() && number.equals("0") || number.isEmpty())
            requireView().showSnackBar("OTP is not valid")
        else validatOtp(otpValue, number)

    }

    private fun validatOtp(otpValue: String, number: String) {
        viewModel.checkOptValue(otpValue, number)
    }


}

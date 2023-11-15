package com.iroid.healthdomain.ui.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.FragmentPhoneNumberBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.utils.enable
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.ui.utils.showSnackBar

class PhoneNumberFragment : BaseFragment<LoginViewModel, FragmentPhoneNumberBinding, LoginRepository>() {

    private lateinit var number: String

    companion object {
        private const val TAG = "PhoneNumberFragment"
        val MOBILE_NUMBER_SIZE = 10
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPhoneNumberBinding {
        return FragmentPhoneNumberBinding.inflate(layoutInflater, container, false)
    }

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getFragmentRepository(): LoginRepository {
        return LoginRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.btGetOtp.enable(false)

        binding.numberEditText.doOnTextChanged { text, start, before, count ->
            if (text.toString().length == MOBILE_NUMBER_SIZE)
                binding.btGetOtp.enable(true)
            else binding.btGetOtp.enable(false)

        }


        binding.btGetOtp.setOnClickListener {


//            NavHostFragment.findNavController(this@PhoneNumberFragment)
//                    .navigate(R.id.action_PhoneNumberFragment_to_stepOneFragment)
            number = binding.numberEditText.text.toString()
            if (isValidMobile(number))
            viewModel.generateOtp(number)
            else requireView().showSnackBar("Invalid phone number")
        }

        addObserver()
    }

    private fun addObserver() {
        viewModel.getOTPResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {

                    viewModel.setLoading(false)

                    val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToOTPFragment(number)
                    NavHostFragment.findNavController(this@PhoneNumberFragment)
                            .navigate(action)

                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
//                    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
//                    snackbar.show()
                    Log.e(TAG, "addObserver: $it")
                    handleApiError(view = requireView(), failure = it)

                }

            }
        })
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}
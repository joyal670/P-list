package com.iroid.healthdomain.ui.profile_set_up

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.FragmentStepFourBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.HomeActivity
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.utils.SimpleRulerView
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.ui.utils.showSnackBar
import com.iroid.healthdomain.ui.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class StepFourFragment :
    BaseFragment<ProfileSetUpViewModel, FragmentStepFourBinding, ProfileSetUpRepository>(),
    SimpleRulerView.OnValueChangeListener {


    val args: StepFourFragmentArgs by navArgs()
    lateinit var accountModel: AccountModel


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepFourBinding {
        return FragmentStepFourBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ProfileSetUpViewModel> {
        return ProfileSetUpViewModel::class.java
    }

    override fun getFragmentRepository(): ProfileSetUpRepository {

        val token = runBlocking { userPreferences.authToken.first().toString() }
        val api = remoteDataSource.buildApi(api = ApiServices::class.java, authToken = token)
        return ProfileSetUpRepository(api, userPreferences)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        accountModel = args.accountModel
        println("Arges : $accountModel")

        binding.btDone.setOnClickListener {

            validation(binding.tvHeight.text.toString())

        }

        addObserver()

        binding.simpleRuler.onValueChangeListener = this

    }

    private fun addObserver() {
        viewModel.getUpdateUserResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)
                }
                is Resource.Success -> {

                    viewModel.setLoading(false)
                    requireContext().showToast(it.value.message)

                    lifecycleScope.launch {
                        println("User frag ${args.accountModel.name}")
                        args.accountModel.name?.let { name -> viewModel.saveUser(name) }
                        AppPreferences.isLogin = true
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()


                    }


                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
                    handleApiError(requireView(), it)
                }

            }
        })
    }

    private fun validation(height: String) {

        if (height == "0" || height == "0.0") {
            requireView().showSnackBar("Please select Height!")
        } else {

            accountModel = AccountModel(
                name = args.accountModel.name,
                age = args.accountModel.age,
                gender = args.accountModel.gender,
                blood_group = args.accountModel.blood_group,
                weight = args.accountModel.weight,
                height = height
            )

            var name: String = args.accountModel.name.toString()
            var age: String = args.accountModel.age.toString()
            var gender: String = args.accountModel.gender.toString()
            var blood_group: String = args.accountModel.blood_group.toString()
            var weight: String = args.accountModel.weight.toString()
            var height: String = height


            viewModel.updateUser(accountModel)


        }

    }

    override fun onChange(view: SimpleRulerView?, position: Int, value: Float) {
        binding.tvHeight.text = value.toString()
    }


}
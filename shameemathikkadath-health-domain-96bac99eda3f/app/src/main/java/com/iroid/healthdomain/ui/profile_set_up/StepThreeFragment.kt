package com.iroid.healthdomain.ui.profile_set_up

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.FragmentStepThreeBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.utils.SimpleRulerView
import com.iroid.healthdomain.ui.utils.showSnackBar
import com.iroid.healthdomain.ui.utils.visible

class StepThreeFragment :
    BaseFragment<ProfileSetUpViewModel, FragmentStepThreeBinding, ProfileSetUpRepository>(),
    SimpleRulerView.OnValueChangeListener {

    val args: StepThreeFragmentArgs by navArgs()

    companion object {
        private const val TAG = "StepThreeFragment"
    }

    lateinit var accountModel: AccountModel

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepThreeBinding {
        return FragmentStepThreeBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ProfileSetUpViewModel> {
        return ProfileSetUpViewModel::class.java
    }

    override fun getFragmentRepository(): ProfileSetUpRepository {
        return ProfileSetUpRepository(
            remoteDataSource.buildApi(ApiServices::class.java),
            userPreferences
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        accountModel = args.accountModel

        println("Arges : $accountModel")

        userPreferences.userStatus.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onActivityCreated: ${it}")
//            if (it == true) binding.tvSkip.visible(true)
//            else binding.tvSkip.visible(false)
        })




        binding.btNext.setOnClickListener {

            validation(binding.tvWeight.text.toString())

        }
        binding.tvSkip.setOnClickListener {
//            NavHostFragment.findNavController(this@StepThreeFragment)
//                    .navigate(R.id.action_StepThreeFragment_to_StepFourFragment)
        }

        binding.simpleRuler.onValueChangeListener = this
    }

    private fun validation(weight: String) {

        if (weight == "0" || weight == "0.0") {
            requireView().showSnackBar("Please select weight")
        } else {
            accountModel = AccountModel(
                name = args.accountModel.name,
                age = args.accountModel.age,
                gender = args.accountModel.gender,
                blood_group = args.accountModel.blood_group,
                weight = weight
            )

            val action =
                StepThreeFragmentDirections.actionStepThreeFragmentToStepFourFragment(accountModel)
            NavHostFragment.findNavController(this@StepThreeFragment)
                .navigate(action)
        }


    }

    override fun onChange(view: SimpleRulerView?, position: Int, value: Float) {
        binding.tvWeight.text = value.toString()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        rulerViewNew = view.findViewById(R.id.ruler_view)
//        tvWeight = view.findViewById(R.id.tv_weight)
//        rulerViewNew.setValuePickerListener(object : RulerValuePickerListener {
//            override fun onValueChange(selectedValue: Int) {
//                //Value changed and the user stopped scrolling the ruler.
//                //Application can consider this value as final selected value.
//                tvWeight.setText(String.format("%d", selectedValue))
//            }
//
//            override fun onIntermediateValueChange(selectedValue: Int) {
//                //Value changed but the user is still scrolling the ruler.
//                //This value is not final value. Application can utilize this value to display the current selected value.
//                tvWeight.setText(String.format("%d", selectedValue))
//            }
//        })
//        view.findViewById<View>(R.id.bt_next).setOnClickListener {
//            NavHostFragment.findNavController(this@StepThreeFragment)
//                    .navigate(R.id.action_StepThreeFragment_to_StepFourFragment)
//        }
//        view.findViewById<View>(R.id.tv_skip).setOnClickListener {
//            NavHostFragment.findNavController(this@StepThreeFragment)
//                    .navigate(R.id.action_StepThreeFragment_to_StepFourFragment)
//        }
//    }
}
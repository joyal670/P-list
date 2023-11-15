package com.iroid.healthdomain.ui.profile_set_up

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.FragmentStepTwoBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.utils.showSnackBar
import com.iroid.healthdomain.ui.utils.visible

class StepTwoFragment : BaseFragment<ProfileSetUpViewModel, FragmentStepTwoBinding, ProfileSetUpRepository>() {

    private var isChecking = true
    private var mCheckedId = 0
    private var mCheckedValue: String = ""

    lateinit var accountModel: AccountModel

    val args: StepTwoFragmentArgs by navArgs()

    companion object{
        private const val TAG = "StepTwoFragment"
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStepTwoBinding {
        return FragmentStepTwoBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ProfileSetUpViewModel> {
        return ProfileSetUpViewModel::class.java
    }

    override fun getFragmentRepository(): ProfileSetUpRepository {
        return ProfileSetUpRepository(remoteDataSource.buildApi(ApiServices::class.java),userPreferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPreferences.userStatus.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onActivityCreated: ${it}")
//            if (it == true) binding.tvSkip.visible(true)
//            else binding.tvSkip.visible(false)
        })

        accountModel = args.accountModel

        println("Arges : $accountModel")

        binding.firstGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                binding.secondGroup.clearCheck()
                mCheckedId = checkedId

                setCheckedValue(mCheckedId)

            }
            isChecking = true
        })

        binding.secondGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                binding.firstGroup.clearCheck()
                mCheckedId = checkedId

                setCheckedValue(mCheckedId)


            }
            isChecking = true
        })



        binding.btNext.setOnClickListener {

            validation(mCheckedValue)

        }
        binding.tvSkip.setOnClickListener {
//            NavHostFragment.findNavController(this@StepTwoFragment)
//                    .navigate(R.id.action_StepTwoFragment_to_StepThreeFragment)
        }


    }

    private fun validation(mCheckedValue: String) {

        if (mCheckedValue.isNullOrEmpty()) requireView().showSnackBar("Please select blood group")
        else {

            accountModel = AccountModel(name = args.accountModel.name,
                    age = args.accountModel.age,
                    gender = args.accountModel.gender,
                    blood_group = mCheckedValue)


            val action = StepTwoFragmentDirections.actionStepTwoFragmentToStepThreeFragment(accountModel)

            NavHostFragment.findNavController(this@StepTwoFragment)
                    .navigate(action)

        }
    }

    private fun setCheckedValue(mCheckedId: Int) {
        val radioButton = binding.root.findViewById<View>(mCheckedId) as RadioButton
        mCheckedValue = radioButton.text.toString()
    }


}
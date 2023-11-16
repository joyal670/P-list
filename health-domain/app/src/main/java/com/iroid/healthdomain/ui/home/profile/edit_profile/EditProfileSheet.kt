package com.iroid.healthdomain.ui.home.profile.edit_profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.model_class.user_profile.UserData
import com.iroid.healthdomain.databinding.SheetEditProfileFragmentBinding

class EditProfileSheet(val listener: onClickListener) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditProfileSheet"
        lateinit var userData: UserData
    }

    private lateinit var binding: SheetEditProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SheetEditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        Log.i(TAG, "onActivityCreated: $userData")

        binding.model = userData

        binding.updateProfileButton.setOnClickListener {
            val weight = binding.weightText.text!!.trim().toString()
            val height = binding.heightText.text!!.trim().toString()
            if (weight.startsWith(".") || weight.startsWith("-") || TextUtils.isEmpty(weight) || height.startsWith(
                    "."
                ) || height.startsWith("-") || TextUtils.isEmpty(height)
            ) {

                if (weight.startsWith(".") || weight.startsWith("-") || TextUtils.isEmpty(weight)) {
                    binding.weightLayout.error = "invalid"
                }

                if (height.startsWith(".") || height.startsWith("-") || TextUtils.isEmpty(height)) {
                    binding.textInputLayout.error = "invalid"
                }

            } else {
                val accountModel = AccountModel(
                    name = userData.name,
                    age = userData.age.toString(),
                    gender = userData.gender,
                    blood_group = userData.blood_group,
                    weight = binding.weightText.text.toString(),
                    height = binding.heightText.text.toString(),
                )
                listener.onUpdate(accountModel)
                this.dismiss()
            }
        }

        binding.weightText.doOnTextChanged { text, start, before, count ->
            binding.weightLayout.isErrorEnabled = false
        }

        binding.heightText.doOnTextChanged { text, start, before, count ->
            binding.textInputLayout.isErrorEnabled = false
        }
    }


    interface onClickListener {
        fun onUpdate(accountModel: AccountModel)
    }

}
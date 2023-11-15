


package com.proteinium.proteiniumdietapp.ui.start_up.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.more.termsAndConditions.TermsAndConditionsActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.login.LoginFragment
import com.proteinium.proteiniumdietapp.utils.*
import com.proteinium.proteiniumdietapp.utils.Constants.ARG_LOGIN
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlin.properties.Delegates

class SignUpFragment : BaseFragment()
{
    private lateinit var signupViewModel: SignupViewModel
    private var isGuest by Delegates.notNull<Boolean>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_signup, container, false)
    }
   /* override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
        setupObserver()
    }*/
    override fun initData() {
        isGuest=requireArguments().getBoolean(ARG_LOGIN)
       if(isGuest){
           tvSkipSignUp.visibility=View.GONE
       }
    }
    companion object{
        fun newInstance(isGuest:Boolean)=SignUpFragment().apply {
            arguments=Bundle().apply {
                putBoolean(ARG_LOGIN,isGuest)
            }
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        signupViewModel= ViewModelProviders.of(this).get(SignupViewModel::class.java)
    }

    override fun setupObserver() {
        signupViewModel.addCustomerSignupResponse().observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("SUCCESS", Gson().toJson(it))
                    if(it.data?.status!!){
                        AppPreferences.token= it.data?.user!!.access_token
                        AppPreferences.user_id=it.data?.user.id
                        AppPreferences.isLogin=true
                        if(isGuest){
                            requireActivity().finish()
                        }else{
                            val intent = Intent(context, HomeActivity::class.java)
                            intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                    Log.e("onSuccess", Gson().toJson(it))
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if(requireActivity().isConnected){
                        CommonUtils.warningToast(requireContext(), getString(R.string.no_internet))

                    }else{
                        CommonUtils.warningToast(requireContext(), getString(R.string.something_wrong))
                    }


                }
                Status.DATA_EMPTY ->{
                    dismissLoader()
                    Log.e("DATA_EMPTY", Gson().toJson(it))
                    CommonUtils.warningToast(requireContext(), getString(R.string.data_empty))
                }
            }
        })
    }

    override fun onClicks() {
        tvSkipSignUp.setOnClickListener {
            AppPreferences.user_id=0
            AppPreferences.token=""
            val intent= Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
            startActivity(intent)
        }
        tvSignUpSignIn.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = LoginFragment.newInstance(false),
                addToBackStack = true)
        }
        btnSignUp.setOnClickListener {
            if(validation()){
                if(etAlternativeNumber.text.toString().trim().isNotEmpty()){
                    if(!isValidMobile(etAlternativeNumber.text.trim().toString())){
                        CommonUtils.warningToast(requireContext(), getString(R.string.invalid_number_alter))
                    }else{
                        signUp()
                    }
                }else{
                   signUp()
                }


            }
        }
        tvTermsAndConditions.setOnClickListener {
            startActivity(Intent(activity,TermsAndConditionsActivity::class.java))
        }

        spGender.setOnClickListener {
            val bottom = BottomSheetDialog(requireContext(),  R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet : View = this.layoutInflater.inflate(R.layout.select_gender, null)

            val maleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderMale)
            val femaleBtn = bottomSheet.findViewById<RadioButton>(R.id.SelectGenderFemale)

            val close =  bottomSheet.findViewById<ImageView>(R.id.ivCloseSelectGender)
            close.setOnClickListener {
                bottom.dismiss()
            }

            val selectBtn =  bottomSheet.findViewById<MaterialButton>(R.id.SelectGenderBtn)
            selectBtn.setOnClickListener {
                if(maleBtn.isChecked)
                {
                    tvGender.text = getString(R.string.male)
                    bottom.dismiss()
                }
                else if(femaleBtn.isChecked)
                {
                    tvGender.text = getString(R.string.female)
                    bottom.dismiss()
                }
                else
                {
                    Toast.makeText(requireContext(), "Select gender", Toast.LENGTH_SHORT).show()
                }


            }

            bottom.setContentView(bottomSheet)
            bottom.show()
        }

    }

    private fun signUp() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                signupViewModel.addCustomerDetails(etFirstName.text.toString().trim(),
                    etMiddleName.text.toString().trim(),
                    etLastName.text.toString().trim(),
                    etEmail.text.toString().trim(), etPhoneNumber.text.toString().trim(),
                    etAlternativeNumber.text.toString().trim(),etPassword.text.toString().trim(),
                   "M",1,
                    task.result!!
                )
            }


        })
    }

    private fun validation(): Boolean {
        Log.e("check checkbox",rbTermsAndCondtion.isChecked.toString())
        if(etFirstName.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_name_first))
            return false
        }
        else if(etMiddleName.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_name_middle))
            return false
        }
        else if(etLastName.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_name_last))
            return false
        }
        else if(etEmail.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_email))
            return false
        }
        else if(!etEmail.text.toString().trim().isEmailValid()){
            CommonUtils.warningToast(requireContext(), getString(R.string.invalid_email))
            return false
        }
        else if(etPassword.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_password))
            return false
        }
        else if(etConfirmPassword.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_confirm_password))
            return false
        }
        else if(etPassword.text.toString().trim().length<6){
            CommonUtils.warningToast(requireContext(), getString(R.string.password_length))
            return false
        }
        else if(etConfirmPassword.text.toString().trim().length<6){
            CommonUtils.warningToast(requireContext(), getString(R.string.password_length))
            return false
        }
        else if(etPassword.text.toString().trim()!=etConfirmPassword.text.toString().trim()){
            CommonUtils.warningToast(requireContext(), getString(R.string.not_same_password))
            return false
        }
        else if(etPhoneNumber.text.toString().trim().isNullOrEmpty()){
            CommonUtils.warningToast(requireContext(), getString(R.string.no_number))
            return false
        }
        else if(!isValidMobile(etPhoneNumber.text.trim().toString())){
            CommonUtils.warningToast(requireContext(), getString(R.string.invalid_number))
            return false
        }


        else if(!rbTermsAndCondtion.isChecked){
            CommonUtils.warningToast(requireContext(), getString(R.string.not_accept_terms))
            return false
        }
        else if(etAlternativeNumber.text.toString().trim().isNotEmpty()){
            if(etPhoneNumber.text.toString().trim()==etAlternativeNumber.text.toString().trim()) {
                CommonUtils.warningToast(requireContext(), getString(R.string.not_same_phone))
                return false
            }
            return true
        }


        else {
            return true
        }
    }
}
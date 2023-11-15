package com.iroid.patrickstore.ui.signup

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivitySignupBinding
import com.iroid.patrickstore.ui.forgotpassword.otp.OtpActivity
import com.iroid.patrickstore.ui.terms_and_conditions.TermsAndConditionActivity
import com.iroid.patrickstore.utils.*


class SignUpActivity : BaseActivity<SignUpViewModel, ActivitySignupBinding>(),
    View.OnClickListener {


    override val layoutId: Int
        get() = R.layout.activity_signup
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false


    private fun setOnTextChanged() {
        binding.etRefferalCode.doOnTextChanged{text, start, before, count ->
            viewModel.onRefferalCodeTextChanged(text.toString())
        }
        binding.etFirstName.doOnTextChanged { text, start, before, count ->
            viewModel.onFirstNameTextChanged(text.toString())
        }
        binding.etLastName.doOnTextChanged { text, start, before, count ->
            viewModel.onLastTextChanged(text.toString())
        }
        binding.etEmail.doOnTextChanged { text, start, before, count ->
            viewModel.onEmailChanged(text.toString())
        }
        binding.etMobile.doOnTextChanged { text, start, before, count ->
            viewModel.onMobileTextChanged(text.toString())
        }
        binding.etPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onPasswordTextChanged(text.toString())
        }

        binding.etConfirmPassword.doOnTextChanged { text, start, before, count ->
            viewModel.onConfirmPasswordTextChanged(text.toString())
        }

        binding.chkTermsAndConditions.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                viewModel.onTermsAndConditionChanged(true)
            } else {
                viewModel.onTermsAndConditionChanged(false)
            }
        }

        viewModel.emptyFirstName?.observe(this, Observer { message ->
            if (message != null)
                binding.tilFirstName.error = message
            else
                binding.tilFirstName.isErrorEnabled = false
        })
        viewModel.emptyLastName?.observe(this, Observer { message ->
            if (message != null)
                binding.tilLastName.error = message
            else
            binding.tilLastName.isErrorEnabled = false
        })
        viewModel.emptyEmail?.observe(this, Observer { message ->
            if (message != null)
                binding.tilEmail.error = message
            else
                binding.tilEmail.isErrorEnabled = false
        })
        viewModel.emptyMobile?.observe(this, Observer { message ->
            if (message != null) {
                binding.tilMobile.error = message
            } else {
                binding.tilMobile.isErrorEnabled = false
            }
        })
        viewModel.emptyPassword?.observe(this, Observer { message ->
            if (message != null) {
                binding.tilPassword.error = message
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
        })
        viewModel.emptyConfirmPassword?.observe(this, Observer { message ->
            if (message != null) {
                binding.tilConfirmPassword.error = message
            } else {
                binding.tilConfirmPassword.isErrorEnabled = false
            }
        })

        viewModel.userTermsAndCondition?.observe(this) {
            if (it == null || it != true) {
                Toaster.showToast(
                    this,
                    TERMS_AND_CONDITION_ERROR,
                    Toaster.State.ERROR,
                    Toast.LENGTH_SHORT
                )
            }
        }
        viewModel.signUpLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra(Constants.INTENT_OTP, it.data!!.data.otp.toString())
                    startActivity(intent)
                    finish()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }

                }


            }
        })


    }

    private fun setOnClick() {
        binding.btnRegister.setOnClickListener {
            setUpRegisterViewModal()
        }
    }

    private fun setUpRegisterViewModal() {
        viewModel.setSignUp(binding.etRefferalCode.text.toString())
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvLogin -> {
                finish()
            }
            binding.tvTermsAndConditions -> {
                startActivity(Intent(this, TermsAndConditionActivity::class.java))
            }
        }
    }

    override fun getViewBinding(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        viewModel = SignUpViewModel()

        binding.tvLogin.setOnClickListener(this)
        binding.tvTermsAndConditions.setOnClickListener(this)

        setOnClick()
        setOnTextChanged()
    }


}

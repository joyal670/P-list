package com.iroid.patrickstore.ui.address.addaddress

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityAddAddressBinding
import com.iroid.patrickstore.model.single_address.Data
import com.iroid.patrickstore.ui.address.AddAddressViewModal
import com.iroid.patrickstore.ui.address.AddressActivity
import com.iroid.patrickstore.ui.location.LocationActivity
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.INTENT_ADDRESS
import com.iroid.patrickstore.utils.Constants.INTENT_TYPE
import kotlinx.android.synthetic.main.activity_add_address.*
import android.R.attr.data
import android.app.Activity
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.iroid.patrickstore.utils.Constants.INTENT_LOCATION


class AddAddressActivity : BaseActivity<AddAddressViewModal, ActivityAddAddressBinding>() {

    private var addressId = ""
    private var isEdit: Boolean = false
    private val REQUEST_LOCATION = 1234

    override val layoutId: Int
        get() = R.layout.activity_add_address
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    private fun setUpViewModal() {

        viewModel.empty_pincode?.observe(this, { message ->
            if (message != null) {
                binding.editPin.error = message.toString()
            } else {
                binding.editPin.error = null
            }

        })
        viewModel.empty_address1?.observe(this, { message ->
            if (message != null) {
                binding.editAddress.error = message.toString()
            } else {
                binding.editAddress.error = null
            }

        })
        viewModel.empty_city?.observe(this, { message ->
            if (message != null) {
                binding.editCity.error = message.toString()
            } else {
                binding.editCity.error = null
            }

        })
        viewModel.empty_state?.observe(this, { message ->
            if (message != null) {
                binding.editState.error = message.toString()
            } else {
                binding.editState.error = null
            }

        })

        viewModel.empty_landMark?.observe(this, { message ->
            if (message != null) {
                binding.editLandMark.error = message.toString()
            } else {
                binding.editLandMark.error = null
            }

        })
        viewModel.empty_label?.observe(this, { message ->
            if (message != null) {
                Toaster.showToast(this, message, Toaster.State.ERROR, Toast.LENGTH_LONG)
            }

        })
        viewModel.empty_contactNumber?.observe(this, { message ->
            if (message != null) {
                binding.editMobile.error = message.toString()
            } else {
                binding.editMobile. error = null
            }

        })


        binding.editPin.doAfterTextChanged {
            viewModel.pinCodeTextChanged(it.toString())
        }
        binding.editAddress.doAfterTextChanged {
            viewModel.onAddressTextChanged(it.toString())
        }
        binding.editCity.doAfterTextChanged {
            viewModel.onCityTextChanged(it.toString())
        }
        binding.editState.doAfterTextChanged {
            viewModel.stateTextChanged(it.toString())
        }
        binding.editLandMark.doAfterTextChanged {
            viewModel.landmarkChanged(it.toString())
        }
        binding.editName.doAfterTextChanged {
            viewModel.onNameTextChanged(it.toString())
        }
        binding.editMobile.doAfterTextChanged {
            viewModel.onContactNumber(it.toString())
        }
    }

    override fun getViewBinding(): ActivityAddAddressBinding {
        return ActivityAddAddressBinding.inflate(layoutInflater)
    }



    override fun initViews() {
        setUpViewModal()
        setUpObserver()
        setOnClick()

        if (intent.getStringExtra(Constants.INTENT_TYPE) == Constants.INTENT_EDIT) {
            isEdit = true
            addressId = intent.getStringExtra(Constants.ADDRESS_ID).toString()
            binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.edit_address)
            viewModel.getSingleAddress(addressId)
        } else {
            isEdit = false
            binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.add_address)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setOnClick() {
        binding.containerNoInternet.btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                viewModel.getSingleAddress(addressId)
            }
        }

        binding.btnHome.setOnClickListener {
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                .also { binding.btnHome.backgroundTintList = it }
            binding.btnHome.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                .also { binding.btnOffice.backgroundTintList = it }
            binding.btnOffice.setTextColor(R.color.colorBlack)
            binding.btnOthers.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            binding.btnOthers.setTextColor(R.color.colorBlack)
            viewModel.addressTypeChanged("Home")

        }
        binding.btnOffice.setOnClickListener {
            binding.btnOffice.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            binding.btnOffice.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                .also { binding.btnHome.backgroundTintList = it }
            binding.btnHome.setTextColor(R.color.colorBlack)
            binding.btnOthers.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            binding.btnOthers.setTextColor(R.color.colorBlack)
            viewModel.addressTypeChanged("Office")

        }
        binding.btnOthers.setOnClickListener {
            binding.btnOthers.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            binding.btnOthers.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            binding.btnOffice.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            binding.btnOffice.setTextColor(R.color.colorBlack)
            binding.btnHome.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            binding.btnHome.setTextColor(R.color.colorBlack)
            viewModel.addressTypeChanged("Others")

        }
        binding.btnSave.setOnClickListener {
            if (isEdit) {
                viewModel.editAddress(addressId)
            } else {
                viewModel.addAddress()
            }
        }

        binding.editLandMark.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra(Constants.INTENT_TYPE,INTENT_ADDRESS)
            resultLauncher.launch(intent)
        }
        binding.constraintLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra(Constants.INTENT_TYPE,INTENT_ADDRESS)
            resultLauncher.launch(intent)
        }
    }

    private fun setUpObserver() {
        viewModel.singleAddressLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    containerNoInternet.isVisible = false
                    containerData.isVisible = true
                    loadAddress(it.data!!.data)
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
                        containerData.isVisible = false
                        containerNoInternet.isVisible = true
                    }
                }
            }
        }

        viewModel.editAddressLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    startActivity(Intent(this, AddressActivity::class.java))
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
                        containerData.isVisible = false
                        containerNoInternet.isVisible = true
                    }
                }
            }
        })

        viewModel.addAddressLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    startActivity(Intent(this, AddressActivity::class.java))
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
                        containerData.isVisible = false
                        containerNoInternet.isVisible = true
                    }
                }
            }
        }
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.e("TAG", ":adfadfs "+ result.resultCode )
        if (result.resultCode == REQUEST_LOCATION) {
            // There are no request codes
            val data: Intent? = result.data
            Log.e("TAG", ": "+ data!!.getStringExtra(INTENT_LOCATION) )
            binding.editLandMark.setText(data!!.getStringExtra(INTENT_LOCATION))

        }
    }
    @SuppressLint("ResourceAsColor")
    private fun loadAddress(data: Data) {
        editPin.setText(data.pincode)
        editAddress.setText(data.address1)
        editCity.setText(data.city)
        editState.setText(data.state)
        editLandMark.setText(data.landMark)
        editName.setText(data.name)
        editMobile.setText(data.contactNumber)

        when (data.label) {
            "Home" -> {
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                    .also { btnHome.backgroundTintList = it }
                btnHome.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                viewModel.addressTypeChanged("Home")
            }
            "Office" -> {
                btnOffice.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                btnOffice.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                viewModel.addressTypeChanged("Office")
            }
            "Others" -> {
                btnOthers.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                btnOthers.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                viewModel.addressTypeChanged("Others")
            }
        }
    }
}

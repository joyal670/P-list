package com.iroid.patrickstore.ui.address

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityAddressBinding
import com.iroid.patrickstore.model.addresslist.Item
import com.iroid.patrickstore.ui.address.adapter.AddressAdapter
import com.iroid.patrickstore.ui.address.addaddress.AddAddressActivity
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.CommonUtils.Companion.setToolbarMargin
import kotlin.math.log

class AddressActivity : BaseActivity<AddAddressViewModal, ActivityAddressBinding>() {

    private var isEdited: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_address
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinding(): ActivityAddressBinding {
        return ActivityAddressBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
        setOnClick()
        binding.rvAddress.addItemDecoration(
            VerticalItemDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )
        binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.my_address)
        setToolbarMargin(this, binding.layoutToolbar.tvToolbarTitle)
        viewModel.getAddressList()
    }
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            Log.e("123456", result.data.toString())
        }
    }

    private fun setOnClick() {
        binding.btnAddAddress.setOnClickListener {

//            startActivity(Intent(this, AddAddressActivity::class.java))
        }
        binding.containerNoInternet.btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                viewModel.getAddressList()
            }
        }
        binding.btnAddress.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
        binding.btnAddAddress


            .setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    private fun setUpObserver() {
        viewModel.addressListLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    binding.containerNoInternet.root.isVisible = false
                    if (it.data!!.data.items.isNullOrEmpty()) {
                        binding.containerData.isVisible = false
                        binding.containerNoData.isVisible = true
                    } else {
                        binding.containerData.isVisible = true
                        setData(it.data.data.items)
                    }
                }
                Status.LOADING -> {
                    if (!isEdited) {
                        showProgress()
                        isEdited = false
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    binding.containerData.isVisible = false
                    binding.containerNoData.isVisible = true
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })

        viewModel.deleteAddressLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.containerNoInternet.root.isVisible = false
                    viewModel.getAddressList()
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })
    }

    private fun setData(items: List<Item>) {
        binding.rvAddress.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAddress.adapter =
            AddressAdapter(this, items, { functionEdit(it) }, { functionDelete(it) },{
                finish()
            })
    }

    private fun functionDelete(it: String) {
        if (it.isNotEmpty()) {
            isEdited = true
            viewModel.deleteAddress(it)
        }
    }

    private fun functionEdit(it: String) {
        if (it.isNotEmpty()) {
            val intent = Intent(this, AddAddressActivity::class.java)
            intent.putExtra(Constants.INTENT_TYPE, Constants.INTENT_EDIT)
            intent.putExtra(Constants.ADDRESS_ID, it)
            startActivity(intent)
            finish()
        }
    }
}

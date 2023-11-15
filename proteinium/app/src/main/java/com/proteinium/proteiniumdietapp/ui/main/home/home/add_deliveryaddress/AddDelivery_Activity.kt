package com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.areas.Area
import com.proteinium.proteiniumdietapp.pojo.blocks.Block
import com.proteinium.proteiniumdietapp.pojo.governorate.Governorates
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.adapter.RecyerviewSelectArea
import com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.adapter.RecyerviewSelectGovernorate
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import kotlinx.android.synthetic.main.activity_add_devlivery_.*
import kotlinx.android.synthetic.main.toolbar_sub.*

class AddDelivery_Activity : BaseActivity() {
    lateinit var bottom: BottomSheetDialog
    private lateinit var addAddressViewModel: AddAddressViewModel
    private var governorsList: ArrayList<Governorates> = ArrayList()
    private var areaList: ArrayList<Area> = ArrayList()
    private var blockList: ArrayList<Block> = ArrayList()
    private var selectedGovernorate: Int = -1
    private var selectedArea: Int = -1
    private var setAsDefaultAddress: Int = 0
    override val layoutId: Int
        get() = R.layout.activity_add_devlivery_
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.add_delivery_address)
        bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        addAddressViewModel.getGovernorates()
        etArea.isEnabled = false

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        addAddressViewModel = ViewModelProviders.of(this).get(AddAddressViewModel::class.java)
    }

    override fun setupObserver() {
        addAddressViewModel.getGovernoratesResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    governorsList.clear()
                    if (it.data?.status!!) {
                        if (!it.data?.data.isNullOrEmpty()) {
                            governorsList = it.data?.data as ArrayList<Governorates>
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message!!)
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        addAddressViewModel.getAreaResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    areaList.clear()
                    if (it.data?.status!!) {
                        if (!it.data?.data.isNullOrEmpty()) {
                            areaList = it.data?.data as ArrayList<Area>
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        addAddressViewModel.getBlockResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    blockList.clear()
                    if (it.data?.status!!) {
                        if (!it.data?.data.isNullOrEmpty()) {
                            blockList = it.data?.data as ArrayList<Block>
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        addAddressViewModel.getAddAddressResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.status!!) {
                        showSuccessMessage(it.data?.message!!)

                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
    }


    override fun onClicks() {
        add_delivery_spinner.setOnClickListener {
            val bottomSheet: View =
                this.layoutInflater.inflate(R.layout.select_delivery_address, null)

            val rvAddress = bottomSheet.findViewById<RecyclerView>(R.id.rvdeliveryAddress)
            val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseAddressDialog)
            val select = bottomSheet.findViewById<MaterialButton>(R.id.SelectAddressDialogBtn)

            rvAddress.layoutManager = LinearLayoutManager(this)

            rvAddress.adapter = RecyerviewSelectGovernorate(governorsList) {
                Log.e("check governorate id", it.toString())
                selectedGovernorate = it
            }

            close.setOnClickListener {
                bottom.dismiss()
            }

            select.setOnClickListener {
                Log.e("check governorate id", selectedGovernorate.toString())
                if (governorsList.size > 0 && selectedGovernorate != -1) {
                    add_delivery_spinner.setText(governorsList[selectedGovernorate].name)
                    etArea.setText("")
                    addAddressViewModel.getAreas(governorsList[selectedGovernorate].id.toString())
                    etArea.isEnabled = true
                }
                bottom.dismiss()
            }


            bottom.setContentView(bottomSheet)
            bottom.show()
        }
        etArea.setOnClickListener {
            val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_area, null)

            val rvArea = bottomSheet.findViewById<RecyclerView>(R.id.rvArea)
            val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseAddressDialog)
            val select = bottomSheet.findViewById<MaterialButton>(R.id.SelectArea)

            rvArea.layoutManager = LinearLayoutManager(this)

            rvArea.adapter = RecyerviewSelectArea(areaList) {
                selectedArea = it
            }

            close.setOnClickListener {
                bottom.dismiss()
            }

            select.setOnClickListener {
                if (areaList.size > 0 && selectedArea != -1) {
                    etArea.setText(areaList[selectedArea].name)
                }
                bottom.dismiss()
            }


            bottom.setContentView(bottomSheet)
            bottom.show()
        }

        btnSubmitAddress.setOnClickListener {
            if (validation()) {
                setAsDefaultAddress = if (switchDefaultAddress.isChecked) 1 else 0
                AppPreferences.user_id?.let { user_id ->
                    addAddressViewModel.addAddress(
                        user_id,
                        governorsList[selectedGovernorate].id,
                        areaList[selectedArea].id,
                        etBlock.text.toString().trim(),
                        et_avenue.text.toString().trim(),
                        et_street.text.toString().trim(),
                        et_building.text.toString().trim(),
                        et_floor.text.toString().trim().toString(),
                        et_appartment.text.toString().trim(),
                        setAsDefaultAddress,
                        et_direction.text.toString()
                    )
                }
            }
        }
    }

    private fun showSuccessMessage(message: String) {
        val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text = message

        yesBtn.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
            finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun validation(): Boolean {

        when {
            add_delivery_spinner.text.toString().trim().isNullOrEmpty() -> {
                CommonUtils.warningToast(this, getString(R.string.empty_governorate))
                return false
            }
            etArea.text.toString().trim().isNullOrEmpty() -> {
                CommonUtils.warningToast(this, getString(R.string.empty_area))
                return false
            }
//            et_avenue.text.toString().trim().isNullOrEmpty() -> {
//                CommonUtils.warningToast(this, getString(R.string.empty_avenue))
//                return false
//            }
            etBlock.text.toString().trim().isNullOrEmpty() -> {
                CommonUtils.warningToast(this, getString(R.string.empty_block))
                return false
            }
            et_street.text.toString().trim().isNullOrEmpty() -> {
                CommonUtils.warningToast(this, getString(R.string.empty_street))
                return false
            }
            et_building.text.toString().trim().isNullOrEmpty() -> {
                CommonUtils.warningToast(this, getString(R.string.empty_building))
                return false
            }
//            et_floor.text.toString().trim().isNullOrEmpty() -> {
//                CommonUtils.warningToast(this, getString(R.string.empty_floor))
//                return false
//            }
//            et_appartment.text.toString().trim().isNullOrEmpty() -> {
//                CommonUtils.warningToast(this, getString(R.string.empty_appartment))
//                return false
//            }
            else -> {
                return true
            }
        }
    }

}

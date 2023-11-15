package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.myaccount_fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.pojo.get_addresses.Address
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isLogin
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.EditAddedAddressActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.CheckoutViewModel
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.AddressAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.activity.DislikesActivity
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.myprofile.MyProfileActivity
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.settings.SettingsActivity
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.activity.SubscriptionHistoryActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.ui.start_up.language_selection.LangaugeSelectionActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Status
import kotlinx.android.synthetic.main.activity_checkout_.*
import kotlinx.android.synthetic.main.fragment_account.*


class MyAccountFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: ActivityListener
    private lateinit var checkoutViewModel: CheckoutViewModel
    private var addressesList: ArrayList<Address> = ArrayList()
    private var selectedAddressId: Int = -1
    var delectStatus: Boolean = false
    lateinit var bottom: BottomSheetDialog

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_account, container, false)
    }

    override fun initData() {
        bottom = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        fragmentTransInterface = activity as HomeActivity
        fragmentTransInterface.setTitle(
            getString(R.string.title_myaccount),
            16f,
            R.font.segoe_ui,
            false
        )
        fragmentTransInterface.setBackButton(false)
        fragmentTransInterface.hideToolbar(false)
        if (isLogin) {
            tvLogout.text = getString(R.string.tvLogout)
        } else {
            tvLogout.text = getString(R.string.login)
        }
        Log.e("123456", "initData: " + AppPreferences.chooseLanguage)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            tvMyProfile.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_account_user),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvDislike.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_dislike_heart),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvSubscriptionHistory.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(
                    R.drawable.ic_subscriptions
                ), null, requireActivity().getDrawable(R.drawable.ic_silver_arrow_right), null
            )
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_settings),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvLogout.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_logout),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvAddress.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_change_password),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )

        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            tvMyProfile.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_account_user),
                null
            )
            tvDislike.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_dislike_heart),
                null
            )
            tvSubscriptionHistory.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(
                    R.drawable.ic_baseline_arrow_forward_silver
                ), null, requireActivity().getDrawable(R.drawable.ic_subscriptions), null
            )
            tvSettings.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_settings),
                null
            )
            tvLogout.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_logout),
                null
            )
            tvAddress.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_change_password),
                null
            )

        }
        tvAddress.setOnClickListener {
            if (isLogin) {
                selectAddress()
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }

        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        checkoutViewModel = ViewModelProviders.of(this).get(CheckoutViewModel::class.java)


    }

    override fun setupObserver() {
        checkoutViewModel.deleteAddressResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    addressesList.clear()
                    if (it.data?.status!!) {
                        bottom.dismiss()
                        AppPreferences.user_id?.let { user_id -> checkoutViewModel.getAddresses(user_id) }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), getString(R.string.no_internet))

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), getString(R.string.data_empty))
                }
            }
        })
        checkoutViewModel.getAddressesResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()

                    addressesList.clear()
                    if (it.data?.status!!) {
                        if (it.data?.data != null) {
                            addressesList.clear()
                            addressesList = it.data?.data as ArrayList<Address>
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(requireContext(), getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    tv_selectAddress.visibility = View.GONE
                }
            }
        })
    }

    override fun onClicks() {
        tvMyProfile.setOnClickListener {
            if (isLogin) {
                startActivity(Intent(requireContext(), MyProfileActivity::class.java))
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }

        }

        tvDislike.setOnClickListener {
            startActivity(Intent(requireContext(), DislikesActivity::class.java))


        }

        tvSubscriptionHistory.setOnClickListener {
            if (isLogin) {
                startActivity(Intent(requireContext(), SubscriptionHistoryActivity::class.java))
            } else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }

        }

        tvSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        tvLogout.setOnClickListener {
            if (isLogin) {
                showExitDialog()
            } else {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }

        }
    }

    private fun showExitDialog() {
        val dialog = context?.let { it1 -> Dialog(it1) }    //  val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.logout_layout)

        val close = dialog?.findViewById(R.id.ivCloseLogout) as ImageView
        val yesBtn = dialog.findViewById(R.id.okBtnLogout) as Button
        val cancelBtn = dialog.findViewById(R.id.cancelBtnLogout) as Button

        dialog.show()

        close.setOnClickListener {
            dialog.dismiss()
        }

        yesBtn.setOnClickListener {
            AppPreferences.logoutClearPreference()
            val intent = Intent(requireContext(), LangaugeSelectionActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.getWindow()?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    private fun selectAddress() {
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_address, null)
        bottom.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val rvAddress = bottomSheet.findViewById<RecyclerView>(R.id.rvAddressList)
        val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseAddress)
        val select = bottomSheet.findViewById<MaterialButton>(R.id.selectAddedBtn)
        select.visibility=View.GONE
        val it: Iterator<Address> = addressesList.iterator()
        it.forEach {
            if (it.default != 1) {
                addressesList.remove(it)
            }
        }

        addressesList.forEach {

        }
        rvAddress.layoutManager = LinearLayoutManager(requireContext())
        rvAddress.adapter = AddressAdapter(addressesList,
            { address_id: Int, address: String ->selectedAddress(address_id,address)  },
            { position: Int, address_id: Int -> deleteAddress(position, address_id) },
            { editAddress(it) })
        close.setOnClickListener {
            bottom.dismiss()
        }
        select.setOnClickListener {
            if (selectedAddressId != -1) {

                checkoutViewModel.setDefaultAddress(AppPreferences.user_id!!, selectedAddressId)
            }
        }
        bottom.setContentView(bottomSheet)
        bottom.show()

    }
    private fun deleteAddress(position: Int, address_id: Int) {
        delectStatus = true
        checkoutViewModel.deleteAddress(address_id)
    }

    override fun onResume() {
        super.onResume()

        if (AppPreferences.isLogin) {
            checkoutViewModel.getAddresses(AppPreferences.user_id!!)
        }


    }

    private fun editAddress(it: Int) {
        bottom.dismiss()
        val intent = Intent(requireContext(), EditAddedAddressActivity::class.java)
        intent.putExtra("address_id", it)
        startActivity(intent)
    }
    private fun selectedAddress(address_id: Int, address: String) {
        selectedAddressId = address_id
    }

}

package com.iroid.emergency.main.home.operation_complete

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentOperationCompleteBinding
import com.iroid.emergency.main.home.HomeViewModal
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import com.iroid.emergency.start_up.utils.isConnected
import com.iroid.emergency.start_up.utils.netDialog

class OperationCompleteFragment : BaseFragment<HomeViewModal, FragmentOperationCompleteBinding>() {
    override fun initViews() {
        binding.tvName.text = requireArguments().getString("name")!!
        binding.tvMobile.text = requireArguments().getString("mobile")!!
        binding.tvAddress.text = requireArguments().getString("address")!!
    }


    override fun setUpObserver() {
        viewModel.completeSecondaryLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showTransferDialog()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR
                    )
                }

                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            "Something went wrong",
                            Toaster.State.ERROR
                        )
                    } else {
                        requireActivity().netDialog(lifecycle)
                    }
                }
            }
        })
        viewModel.completeNormalLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showTransferDialog()
                }
                Status.ERROR -> {
                    dismissProgress()
                }

                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            "Something went wrong",
                            Toaster.State.ERROR
                        )
                    } else {
                        requireActivity().netDialog(lifecycle)
                    }
                }
            }
        })
    }

    override fun setOnClick() {
        binding.btnComplete.setOnClickListener {
            viewModel.completeNormal(requireArguments().getString("request_id")!!)
        }
        binding.btnAmbulance.setOnClickListener {
            viewModel.completeSecondary(requireArguments().getString("request_id")!!)
        }
        binding.btnRoute.setOnClickListener {
            val gmmIntentUri = Uri.parse(
                "geo:${requireArguments().getString("lat")!!},${
                    requireArguments().getString("lan")!!
                }"
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)


        }
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${requireArguments().getString("mobile")!!}")
            startActivity(intent)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        isEnabled = false
                        findNavController().navigate(R.id.nav_home)
                    }
                }
            }
            )
    }

    override fun getViewBinding(): FragmentOperationCompleteBinding {
        return FragmentOperationCompleteBinding.inflate(layoutInflater)
    }

    private fun showTransferDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_transffered)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params
        val btnTransfer = dialog.findViewById<Button>(R.id.btnTransfer)

        btnTransfer.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.nav_home)
        }

        dialog.show()
    }
}

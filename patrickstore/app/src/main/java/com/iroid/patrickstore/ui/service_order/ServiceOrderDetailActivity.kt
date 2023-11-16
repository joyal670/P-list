package com.iroid.patrickstore.ui.service_order

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomedialog.*
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityServiceOrderDetailBinding
import com.iroid.patrickstore.preference.ConstantPreference
import com.iroid.patrickstore.ui.cart.order_summary.OrderSummaryActivity
import com.iroid.patrickstore.ui.service_order.adapter.ServicePaymentAdapter
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.BUNDLE_TYPE_SERVICE


class ServiceOrderDetailActivity : BaseActivity<ServiceOrderViewModal,ActivityServiceOrderDetailBinding>() {

    private lateinit var tvSelectTime:EditText
    private lateinit var  tvComments:EditText
    private lateinit var dialog: Dialog
    private lateinit var quoteId:String
    private lateinit var type:String
    override val layoutId: Int
        get() = R.layout.activity_service_order_detail
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        setUpObserver()
        setServiceOrderAdapter()
        supportActionBar?.title =getString(R.string.service_order_detail)
        intent.getStringExtra(Constants.INTENT_TYPE).let {

            quoteId= it.toString()
            viewModel.getServiceDetail(it!!)
        }

        binding.btnBargain.setOnClickListener {
            type=ConstantPreference.NEGOTIATED
            showBargainDialog()
        }
        binding.btnReject.setOnClickListener {
            type=ConstantPreference.REJECTED
            viewModel.updateServiceLiveData(quoteId, ConstantPreference.REJECTED,"","")
        }


        binding.btnAccept.setOnClickListener {
            type=ConstantPreference.ACCEPTED
            viewModel.updateServiceLiveData(quoteId, ConstantPreference.ACCEPTED,"","")
        }

    }
    private fun setUpObserver() {
        viewModel.serviceUpdateLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    if(type==ConstantPreference.ACCEPTED){
                        showSuccessDialog(it.data!!.msg)
                    }
                    else if(type==ConstantPreference.REJECTED){
                        showRejectDialog(it.message)
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{
                    if(type==ConstantPreference.ACCEPTED){
                        val bundle = Bundle()
                        bundle.putString(Constants.BUNDLE_TYPE, BUNDLE_TYPE_SERVICE)
                        bundle.putString(Constants.BUNDLE_KEY_QUOTE_ID, quoteId)
                        val intent = Intent(this, OrderSummaryActivity::class.java)
                        intent.putExtra(Constants.BUNDLE_AMOUNT, bundle)
                        startActivity(intent)
                    }else{
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }

                }
                Status.NO_INTERNET->{

                }
            }
        })
        viewModel.serviceOrderDetailData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    binding.tvServiceName.text=it.data!!.data.serviceId.serviceName
                    binding.tvQty.text=it.data!!.data.quantity.toString()
                    binding.tvServiceTime.text=it.data!!.data.time

                    val controller: LayoutAnimationController =
                        AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
                    binding.rvPayment.layoutAnimation = controller
                    binding.rvPayment.layoutManager =

                        LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)

                    binding.rvPayment.adapter=ServicePaymentAdapter(this,
                    it.data.data.responseHistory)

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
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
    private fun setServiceOrderAdapter() {


    }


    override fun getViewBinding(): ActivityServiceOrderDetailBinding {
        return ActivityServiceOrderDetailBinding.inflate(layoutInflater)
    }
    private fun showBargainDialog(){
        dialog = this.let { it1 -> Dialog(it1) }
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_dialog_service_bargain)
        tvSelectTime = dialog.findViewById(R.id.tvNewPrice) as EditText
        tvComments = dialog.findViewById(R.id.tvComments) as EditText
        val btnContinue = dialog.findViewById(R.id.btnContinue) as Button

        btnContinue.setOnClickListener {
            viewModel.updateServiceLiveData(quoteId, ConstantPreference.NEGOTIATED,tvSelectTime.text.toString(),"")
            dialog.dismiss()
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showSuccessDialog(message: String?) {
        AwesomeDialog.build(this)
            .title("Accepted")
            .body(message!!)
            .icon(R.drawable.ic_congrts)
            .onPositive("Go To Home") {
                finish()
            }
    }
    private fun showRejectDialog(message: String?) {
        AwesomeDialog.build(this)
            .title("Rejected")
            .body(message!!)
            .icon(R.drawable.ic_dot_red)
            .onPositive("Go To Home") {
                finish()
            }
    }

}

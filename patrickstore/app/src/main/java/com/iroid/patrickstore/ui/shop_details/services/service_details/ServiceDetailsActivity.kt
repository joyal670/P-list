package com.iroid.patrickstore.ui.shop_details.services.service_details

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.awesomedialog.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityServiceDetailsBinding
import com.iroid.patrickstore.model.service.service_list.Item
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.utils.*
import java.text.SimpleDateFormat
import java.util.*

class
ServiceDetailsActivity :
    BaseActivity<ServiceDetailViewModal, ActivityServiceDetailsBinding>() {
    private lateinit var serviceId: String
    private lateinit var maxPrice: String
    private var checkDate: Boolean = false
    private var checkTime: Boolean = false
    private var date: String = ""
    private lateinit var tvSelectTime: TextView
    private lateinit var tvComments: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQty: EditText
    private lateinit var dialog: Dialog
    override val layoutId: Int
        get() = R.layout.activity_service_details
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.let {
            val service: Item = it.getSerializableExtra(Constants.INTENT_SERVICE) as Item
            setData(service)

        }
        binding.btnQuote.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun setData(service: Item) {
        CommonUtils.setImage(this, service.imgUrl[0].publicUrl, binding.ivServiceBanner)
        binding.textView7.text = service.serviceName
        binding.tvRate.text = CommonUtils.getCurrencyFormat(service.maxPrice.toString())
        binding.tvDescription.text = service.description
        serviceId = service.id
        maxPrice = service.maxPrice.toString()


    }

    override fun onResume() {
        super.onResume()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menue_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cart -> {
            startActivity(Intent(this, CartActivity::class.java))
            true
        }
        R.id.action_call -> {
            true
        }
        R.id.action_profile -> {
            startActivity(Intent(this, MyAccountActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun getViewBinding(): ActivityServiceDetailsBinding {
        return ActivityServiceDetailsBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.include3.tvToolbarTitle.text=getString(R.string.service_details)
    }

    private fun setUpObserver() {
        viewModel.serviceQuoteLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showSuccessDialog(it.data!!.msg)


                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }

            }
        })
    }

    private fun showSuccessDialog(message: String?) {
        AwesomeDialog.build(this)
            .title("Successful")
            .body(message!!)
            .icon(R.drawable.ic_congrts)
            .onPositive("Go To Service") {
                finish()
            }
    }


    private fun showInfoDialog() {
        dialog = this.let { it1 -> Dialog(it1) }
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_dialog_service_date_time)

        val tvSelectDate = dialog.findViewById(R.id.tvSelectDate) as TextView
        tvSelectTime = dialog.findViewById(R.id.tvSelectTime) as TextView
        tvComments = dialog.findViewById(R.id.tvComments) as EditText
        etPrice=dialog.findViewById(R.id.etPrice) as EditText
        etQty=dialog.findViewById(R.id.etQty) as EditText
        val btnContinue = dialog.findViewById(R.id.btnContinue) as Button

        btnContinue.setOnClickListener {
            actionData()
        }
        tvSelectDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val picker = builder.build()
            this?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH) // modify format
                    date = formatter.format(Date(it))
                    tvSelectDate.text = date
                    tvSelectDate.setTextColor(Color.BLACK)
                    checkDate = true
                    tvSelectTime.isEnabled = true
                    checkTime = false
                    tvSelectTime.text = getString(R.string.spTimeSlotHint)
                    tvSelectTime.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray2
                        )
                    )
                }

            }
        }
        tvSelectTime.setOnClickListener {
            val picker =
                MaterialDatePicker
                    .Builder
                    .dateRangePicker()
                    .setTitleText("Select a date")
                    .build()
            supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                // call back code
                var newHour: String = picker.
                var newMinute: String = picker.minute.toString()
                val temp = Calendar.getInstance()
                temp[Calendar.HOUR_OF_DAY] = newHour.toInt()
                temp[Calendar.MINUTE] = newMinute.toInt()
                if (date == this.getCurrentDateOtherFormat()) {
                    if (temp.after(GregorianCalendar.getInstance())) {
                        if (newHour.length == 1) {
                            newHour = "0$newHour"
                        }
                        if (newMinute.length == 1) {
                            newMinute = "0$newMinute"
                        }
                        Log.e("time", "$newHour:$newMinute")
                        checkTime = true
                        tvSelectTime.text = "$newHour:$newMinute"
                        tvSelectTime.setTextColor(Color.BLACK)
                    } else {
                        Toaster.showToast(
                            this,
                            getString(R.string.cannot_select_past_time),
                            Toaster.State.WARNING,
                            Toast.LENGTH_SHORT
                        )

                        checkTime = false
                        tvSelectTime.text = getString(R.string.spTimeSlotHint)
                        tvSelectTime.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.dark_gray2
                            )
                        )
                    }
                } else {
                    if (newHour.length == 1) {
                        newHour = "0$newHour"
                    }
                    if (newMinute.length == 1) {
                        newMinute = "0$newMinute"
                    }
                    Log.e("time", "$newHour:$newMinute")
                    checkTime = true
                    tvSelectTime.text = "$newHour:$newMinute"
                    tvSelectTime.setTextColor(Color.BLACK)
                }
            }
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    private fun actionData() {
        if (!checkDate) {
            Toaster.showToast(
                this, getString(R.string.date_required),
                Toaster.State.ERROR, Toast.LENGTH_LONG
            )
        } else if (!checkTime) {
            Toaster.showToast(
                this, getString(R.string.time_required),
                Toaster.State.ERROR, Toast.LENGTH_LONG
            )
        } else {
            dialog.dismiss()
            viewModel.addServiceQuote(
                serviceId, AppPreferences.sellerId!!, etPrice.text.toString(),
                date,
                tvSelectTime.text.toString(),
                etQty.text.toString(),
                tvComments.text.toString()
            )
        }
    }

}

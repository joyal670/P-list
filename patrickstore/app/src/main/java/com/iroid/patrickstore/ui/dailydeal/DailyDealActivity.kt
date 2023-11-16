package com.iroid.patrickstore.ui.dailydeal

import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityDailyDealsBinding
import com.iroid.patrickstore.model.daily_deal.DailyDeal
import com.iroid.patrickstore.model.daily_deal.DailyDealType
import com.iroid.patrickstore.model.daily_deal.ProductDailyDeal
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.dailydeal.adapter.DailyDealAdapter
import com.iroid.patrickstore.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DailyDealActivity : BaseActivity<DailyDealViewModal, ActivityDailyDealsBinding>() {

    private lateinit var countDownTimer: CountDownTimer

    override val layoutId: Int
        get() = R.layout.activity_daily_deals
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false


    override fun getViewBinding() = ActivityDailyDealsBinding.inflate(layoutInflater)

    override fun initViews() {
        binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.daily_deals)
        CommonUtils.setToolbarMargin(this, binding.layoutToolbar.tvToolbarTitle)

        setUpObserver()
        viewModel.getDailyDeal()

    }

    fun setUpObserver() {
        viewModel.dailyDealLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setDailyDeal(it.data?.data!!.items)
                    printDifferenceDateForHours()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    binding.noData.visibility= View.VISIBLE
                    binding.mainData.visibility=View.GONE

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

                    }
                }
            }
        })
    }

    private fun setDailyDeal(dailyDeal: List<DailyDeal>) {
        val dailyDealModified = ArrayList<DailyDealType>()
        dailyDeal.forEach {
            dailyDealModified.add(DailyDealType(it._id,ArrayList<ProductDailyDeal>(),it.sellerImage,it.sellerName,1))
            dailyDealModified.add(DailyDealType(it._id,it.products,it.sellerImage,it.sellerName,2))
        }
        binding.rvDailyDeal.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvDailyDeal.adapter =
            DailyDealAdapter(this, dailyDealModified)
    }
    fun printDifferenceDateForHours() {

        val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = format1.format(Calendar.getInstance().time)
        val currentTime = format1.parse(currentDate)
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR,1)
        c.add(Calendar.HOUR_OF_DAY,0)
        c.add(Calendar.MINUTE,0)
        c.add(Calendar.SECOND,0)
        c.add(Calendar.MILLISECOND,0)
        val tomorrowDate= format2.format(c.time)
        val tommorowTime = format1.parse("$tomorrowDate 00:00:00")


        //milliseconds
        var different = tommorowTime.time-currentTime.time
        countDownTimer = object : CountDownTimer(different, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                binding.tvD1.text = "$elapsedDays"
                binding.tvD2.text = "$elapsedHours"
                binding.tvD3.text = "$elapsedMinutes"
                binding.tvD4.text = "$elapsedSeconds"
            }

            override fun onFinish() {

            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

}

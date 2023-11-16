package com.iroid.healthdomain.ui.home.my_health

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.SingleValueDataSet
import com.anychart.graphics.vector.SolidFill
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ekn.gruzer.gaugelibrary.Range
import com.example.awesomedialog.*
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.dummyModel.FitModel
import com.iroid.healthdomain.data.model_class.index.ActiveChallenge
import com.iroid.healthdomain.data.model_class.index.PastChallenge
import com.iroid.healthdomain.data.model_class.index.PendingChallenge
import com.iroid.healthdomain.data.model_class.index.Target
import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.*
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.HomeActivity
import com.iroid.healthdomain.ui.home.fit.ActivityTracker
import com.iroid.healthdomain.ui.home.my_health.active_past.ActivePastViewPagerFragment
import com.iroid.healthdomain.ui.home.my_health.challenges.ChallengesFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.HomeViewPagerFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.calories.CaloriesFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.utils.CommonUtils
import com.iroid.healthdomain.ui.utils.handleApiError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.measureNanoTime

class MyHealthFragment :
    BaseFragment<MyHealthViewModel, MyHealthFragmentBinding, MyHealthRepository>(),
    ActivityTracker.onBroadCast {

    companion object {
        fun newInstance() = MyHealthFragment()
        private const val TAG = "MyHealthFragment"
    }

    var userImage = ""
    var userName = ""
    var userPoint = ""
    var mThread: Thread? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MyHealthFragmentBinding {
        return MyHealthFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<MyHealthViewModel> {
        return MyHealthViewModel::class.java
    }

    override fun getFragmentRepository(): MyHealthRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        println("Token : ${token}")
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return MyHealthRepository(api, userPreferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getDate()

        ActivityTracker.onClickReceived(this)

        userPreferences.name.asLiveData().observe(viewLifecycleOwner, Observer {

        })


        lifecycleScope.launch {
            val time = measureNanoTime {

                async { viewModel.getIndexApi() }.await()
                async { viewModel.getUserProfile() }.await()

            }
            println("Time $time")

        }



        addObserver()

    }

    override fun onResume() {
        super.onResume()
        if(AppPreferences.isLoaded){
            AppPreferences.isLoaded=false
            lifecycleScope.launch {
                val time = measureNanoTime {

                    async { viewModel.getIndexApi() }.await()
                    async { viewModel.getUserProfile() }.await()

                }
                println("Time $time")

            }
        }
    }

    fun updateSteps(fitModel: FitModel) {
        viewModel.setSteps(fitModel)
    }

    private fun getDate() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        binding.materialTextView2.text = currentDate
    }



    private fun addObserver() {
        viewModel.indexApiResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    initCircularGauge(it.value.data.HDS.toString().toDouble().roundToInt())
                    launchActivePastFragment(
                        it.value.data.active_challenges,
                        it.value.data.past_challenges
                    )
                    binding.model = it.value.data
                    binding.materialTextView17.text =
                        it.value.data.HDS.toString().toDouble().roundToInt().toString()
                    when {
                        it.value.data.HDS.toString().toDouble().roundToInt() <= 25 -> {
                            binding.materialTextView3.text = "Alert"
                            binding.materialTextView5.text = "Your fitness status is Bad"
                            GlobalScope.launch {
                                userPreferences.saveHdStatus("Your fitness status is Bad")
                            }
                        }
                        it.value.data.HDS.toString().toDouble().roundToInt() <= 50 -> {
                            binding.materialTextView3.text = "Just a little more"
                            binding.materialTextView5.text = "Your fitness status is poor"
                            GlobalScope.launch {
                                userPreferences.saveHdStatus("Your fitness status is poor")
                            }
                        }
                        it.value.data.HDS.toString().toDouble().roundToInt() <= 75 -> {
                            binding.materialTextView3.text = "Well done"
                            binding.materialTextView5.text = "Your fitness status is fair"
                            GlobalScope.launch {
                                userPreferences.saveHdStatus("Your fitness status is fair")
                            }
                        }
                        else -> {
                            binding.materialTextView3.text = "You are a star"
                            binding.materialTextView5.text = "Your fitness status is fair"
                            GlobalScope.launch {
                                userPreferences.saveHdStatus("Your fitness status is fair")
                            }
                        }
                    }


                    launchViewPagerFragment(it.value.data.targets)
                    AppPreferences.key_user_id = it.value.data.userID.toString()
                    kotlinx.coroutines.GlobalScope.launch {
                        it.value.data.userID.let { it1 -> userPreferences.saveUserId(it1) }
                    }

                    //Will show pending challenges in Challenge fragment
                    launchChallengesFragment(it.value.data.pending_challenges)

                    if (it.value.data.pending_challenges.isNotEmpty()) {
                        if (it.value.data.pending_challenges[0].initiated_user == it.value.data.pending_challenges[0].oponentID && it.value.data.pending_challenges[0].status == "INITIATED") {
                            GlobalScope.launch {
                                userPreferences.saveNotification(true)
                            }

                            showChallengePopUp(
                                it.value.data.pending_challenges[0],
                                it.value.data.points
                            )


                        }
                    }
                    /*  if (it.value.data.challenges != null) {
                         launchChallengesFragment(it.value.data.challenges)
                     }*/

                }
                is Resource.Failure -> {
                    handleApiError(requireView(), it)
                }
            }
        })

        viewModel.userApiResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> viewModel.setLoading(true)
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        viewModel.setLoading(false)
                        binding.userModel = it.value.data

                        userImage = it.value.data.profile_image_url.toString()
                        userName = it.value.data.name.toString()
                        userPoint = it.value.data.points.toString()
                        if (it.value.data.profile_image_url == null) {
                            HomeActivity().setDrawerHeader(
                                "",
                                it.value.data.name!!
                            )
                        } else {
                            HomeActivity().setDrawerHeader(
                                it.value.data.profile_image_url,
                                it.value.data.name!!
                            )
                        }

                    }
                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
                    handleApiError(requireView(), it)
                }
            }
        }

        viewModel.stepsLiveData.observe(viewLifecycleOwner, Observer {


            if (it != null) {

                // viewModel.sendStepsToBackend(it.steps.toString())

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())

                val updateData = SendStepsUpdates(1, 0, currentDate, 30, it.steps.toString())

                Log.i(TAG, "addObserver123: $updateData")
                viewModel.sendSteps(updateData)
                //  viewModel.sendSteps(it.steps.toString(),currentDate)
                // viewModel.sendStepsToBackend(it.steps.toString())
            }
        })

        viewModel.updateActivityResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        Log.i(TAG, "addObserver: Updated...")
                    }
                }
                is Resource.Failure -> {

                    handleApiError(requireView(), it)
                }
            }
        }

        viewModel.updateStepsActivityResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        Log.i(TAG, "addObserver: Updated...")
                    }
                }
                is Resource.Failure -> {
                    //     handleApiError(requireView(), it)
                }
            }
        }

        viewModel.updateChallengeApiResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> viewModel.setLoading(true)
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        viewModel.setLoading(false)
                        reloadApi()
                    }
                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
                    handleApiError(requireView(), it)
                }
            }
        }
    }

    private fun reloadApi() {
        lifecycleScope.launch {
            val time = measureNanoTime {

                async { viewModel.getIndexApi() }.await()
                async { viewModel.getUserProfile() }.await()

            }
            println("Time $time")

        }
    }

    private fun showChallengeUpdateDialog() {
        AwesomeDialog.build(requireActivity())
            .title("Congratulations..!")
            .body("Challenge updated successfully.")
            .icon(R.drawable.ic_congrts)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Continue", buttonBackgroundColor = R.drawable.drawable_button) {
                requireActivity().finish()
                startActivity(requireActivity().intent)
                requireActivity().overridePendingTransition(0, 0)
            }
    }

    private fun showChallengePopUp(pendingChallenge: PendingChallenge, points: Int) {
        val binding: DialogChallengeRequestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.dialog_challenge_request, null, false
        )
        binding.materialButton.text = pendingChallenge.opponent_points.toString()
        binding.userModel = pendingChallenge
        val dialog = Dialog(requireActivity())
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        binding.btnAccept.setOnClickListener {
            val pos: Int = binding.segmentedButtonGroup.position
            dialog.dismiss()
            setUpChallengeDialog(pos, pendingChallenge)

        }
        binding.btnDecline.setOnClickListener {
            val pos: Int = binding.segmentedButtonGroup.position
            dialog.dismiss()
            setUpDeclineDialog(pos, pendingChallenge)
        }

        binding.segmentedButtonGroup.setOnPositionChangedListener { pos ->
            if (pos == 0) {
                binding.image5.visibility = View.VISIBLE
                binding.image4.visibility = View.VISIBLE
            } else {
                binding.image5.visibility = View.GONE
                binding.image4.visibility = View.GONE
            }
        }
    }

    /* decline challenge dialog */
    private fun setUpDeclineDialog(pos: Int, pendingChallenge: PendingChallenge) {
        val binding: DialogChallengeDeclineBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_challenge_decline,
            null,
            false
        )
        val dialog = Dialog(requireActivity())
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        GlobalScope.launch {
            userPreferences.saveNotification(false)
        }

        viewModel.updateChallenge(pendingChallenge.id.toString(), "DECLINED")
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 5000)
    }

    /* accept challenge dialog */
    private fun setUpChallengeDialog(pos: Int, pendingChallenge: PendingChallenge) {
        /* 0 -> instant challenge */
        /* 1 -> 10 days later */
        when (pos) {
            0 -> {
                val binding: DialogChallengeSuccessBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_challenge_success,
                    null,
                    false
                )
                binding.materialButtonOpponent.text = pendingChallenge.opponent_points.toString()
                binding.userModel = pendingChallenge
                val dialog = Dialog(requireActivity())
                dialog.setCancelable(true)
                dialog.setContentView(binding.root)
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window?.setLayout(width, height)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(false)
                dialog.show()
                Glide.with(this).load(userImage)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placehold)
                    .into(binding.profileImageOpponent)

                binding.materialButtonOpponent.text=userPoint
                binding.materialTextViewOpponent.text=userName
                binding.materialButton.text = pendingChallenge.opponent_points.toString()

                binding.tvStatus.text = "Challenging"
                var i = 0
                val timer = object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        i++
                        binding.progressBar.setProgressPercentage(
                            i * 100 / (3000 / 1000).toDouble(),
                            true
                        )
                    }

                    override fun onFinish() {
                        dialog.dismiss()
                        when {
                            pendingChallenge.opponent_points.toString() > userPoint -> {
                                binding.tvStatus.text = "LOST"
                            }
                            pendingChallenge.opponent_points.toString() < userPoint -> {
                                binding.tvStatus.text = "WON"
                            }
                            else -> {
                                binding.tvStatus.text = "EQUAL POINTS"
                            }
                        }

                        GlobalScope.launch {
                            userPreferences.saveNotification(false)
                        }
                        if (pos == 0) {
                            viewModel.updateChallenge(pendingChallenge.id.toString(), "ACCEPTED")
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                        }, 2000)


                    }
                }
                timer.start()
            }

            1 -> {
                val binding: DialogChallengeSuccessAfterTenDaysBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_challenge_success_after_ten_days,
                    null,
                    false
                )
                val dialog = Dialog(requireActivity())
                dialog.setCancelable(true)
                dialog.setContentView(binding.root)
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window?.setLayout(width, height)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(false)
                dialog.show()

                val timer = object : CountDownTimer(3000, 5000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        dialog.dismiss()
                        GlobalScope.launch {
                            userPreferences.saveNotification(false)
                        }
                        if (pos == 1) {
                            viewModel.updateChallenge(
                                pendingChallenge.id.toString(),
                                "ACCEPTED_LATER"
                            )
                        }

                        Handler(Looper.getMainLooper()).postDelayed({
                        }, 2000)
                    }
                }
                timer.start()

            }
        }
    }


    private fun setGouge() {
        val range = Range()
        range.color = Color.parseColor("#f73637")
        range.from = 0.0
        range.to = 25.0

        val range2 = Range()
        range2.color = Color.parseColor("#fd823f")
        range2.from = 25.0
        range2.to = 50.0

        val range3 = Range()
        range3.color = Color.parseColor("#fffa4a")
        range3.from = 50.0
        range3.to = 75.0

        val range4 = Range()
        range4.color = Color.parseColor("#67fd4a")
        range4.from = 75.0
        range4.to = 100.0

        //add color ranges to gauge
        //add color ranges to gauge
//        binding.halfGauge.addRange(range)
//        binding.halfGauge.addRange(range2)
//        binding.halfGauge.addRange(range3)
//        binding.halfGauge.addRange(range4)

        //set min max and current value
        //set min max and current value
//        binding.halfGauge.minValue = 0.0
//        binding.halfGauge.maxValue = 100.00
//        binding.halfGauge.value = 25.00
//
//
//
//
//        binding.halfGauge.setNeedleColor(Color.DKGRAY)
//        binding.halfGauge.valueColor = Color.BLUE
//        binding.halfGauge.minValueTextColor = Color.RED
//        binding.halfGauge.maxValueTextColor = Color.GREEN
    }

    private fun launchViewPagerFragment(data: List<Target>) {

        HdsFragment.list = data
        StepsFragment.list = data
        CaloriesFragment.list = data

        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.historyActivityFragment,
            HomeViewPagerFragment.newInstance(), HomeViewPagerFragment::class.java.toString()
        ).commit()
    }


    private fun launchChallengesFragment(challenges: List<PendingChallenge>) {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.ChallengesFragment,
            ChallengesFragment.newInstance(challenges), ChallengesFragment::class.java.toString()
        ).commit()
    }

    private fun launchActivePastFragment(
        activeChallenges: List<ActiveChallenge>,
        pastChallenges: List<PastChallenge>
    ) {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.activePastFragment,
            ActivePastViewPagerFragment.newInstance(activeChallenges, pastChallenges),
            ActivePastViewPagerFragment::class.java.toString()
        ).commit()

    }

    /* private fun launchActivePastFragment(contacts: List<Contact>) {
         requireActivity().supportFragmentManager.beginTransaction().replace(
             R.id.activePastFragment,
             ActivePastViewPagerFragment.newInstance(contacts),
             ActivePastViewPagerFragment::class.java.toString()
         ).commit()
     }*/

    private fun initCircularGauge(hds: Int) {

        val unicode = 0x2B50
        val emojiStar: String = getEmojiByUnicode(unicode).toString()

        val singleStar = emojiStar
        val doubleStar = "$emojiStar $emojiStar"
        val tripleStar = "$emojiStar $emojiStar $emojiStar"

        val circularGauge = AnyChart.circular()
        circularGauge.fill("#fff")
            .stroke("null")
            .margin(6, 6, 6, 6)


        //hdsConstantMain & hdsConstantFinal are range interval values used for custom range selection
        //Separate Calculation is done for range in order to implement multiple ranges in semi-pie chart(AnyChart)

        val hdsConstantMain = 2.6664
        val hdsConstantFinal = 0.4444
        // val currentValue = 20
        val currentValue = hds
        var finalValue: Double = currentValue.toDouble()
        when (currentValue) {
            0 -> {
                finalValue = currentValue.toDouble()
            }
            in 0..100 -> {
                finalValue = currentValue * hdsConstantMain
            }
            in 101..400 -> {
                val starValue = currentValue - 100
                finalValue = starValue * hdsConstantFinal
                finalValue += 266.64
            }
        }

        Log.i(TAG, "initCircularGauge: ${finalValue.toInt()}")

        //val finalValue = currentValue
        // circularGauge.data(SingleValueDataSet(arrayOf(currentValue)))
        circularGauge.data(SingleValueDataSet(arrayOf(finalValue.toInt())))

        circularGauge.axis(0)
            .startAngle(-90)
            .radius(80)
            .sweepAngle(180)
            .width(0)
            .drawFirstLabel(true)

        circularGauge.axis(0).labels()
            .position("inside")
            .enabled(false)
            .useHtml(true)
        circularGauge.axis(0).labels().format()

        circularGauge.axis(0).scale()
            .minimum(0)
            .maximum(400)
            .startAutoCalc()

        circularGauge.axis(0).scale()
        //.ticks("{interval: 25}")

        circularGauge.marker(0)
            .fill(SolidFill("#ed3f39", 1))
            .stroke("null")
        circularGauge.marker(0)
            .size(7)
            .radius(80)

        circularGauge.range(
            0,
            "{\n" +
                    "    from: 0,\n" +
                    "    to: 66.66,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#f9383d',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    gap: 25,\n" +
                    "    zIndex: 1\n" +
                    "  }"
        )

        circularGauge.range(
            1,
            ("{\n" +
                    "    from: 66.6,\n" +
                    "    to: 133.32,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#dd5853',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )

        circularGauge.range(
            2,
            ("{\n" +
                    "    from: 133.32,\n" +
                    "    to: 199.98,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#e17036',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )


        circularGauge.range(
            3,
            ("{\n" +
                    "    from: 199.98,\n" +
                    "    to: 266.64,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#f4b744',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )

        circularGauge.range(
            4,
            ("{\n" +
                    "    from: 266.64,\n" +
                    "    to: 311.08,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#3b8248',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )

        circularGauge.range(
            5,
            ("{\n" +
                    "    from: 311.08,\n" +
                    "    to: 355.52,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#2d542f',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )

        circularGauge.range(
            6,
            ("{\n" +
                    "    from: 355.52,\n" +
                    "    to: 400,\n" +
                    "    position: 'outside',\n" +
                    "    fill: '#192c15',\n" +
                    "    stroke: '1 #000',\n" +
                    "    startSize: 40,\n" +
                    "    endSize: 40,\n" +
                    "    radius: 80,\n" +
                    "    zIndex: 1\n" +
                    "  }")
        )

        //This label is used to show status label


        circularGauge.label(0)
            .text("BAD")
            .fontColor("#ffffff")
            .fontSize(14)
            .fontStyle("bold")
            .rotation(285)
            .position("inside")
            .offsetY("95%")
            .offsetX(-75)
            .anchor("center")

        circularGauge.label(1)
            .text("POOR")
            .fontColor("#ffffff")
            .fontSize(14)
            .fontStyle("bold")
            .rotation(315)
            .position("inside")
            .offsetY("95%")
            .offsetX(-45)
            .anchor("center")

        circularGauge.label(2)
            .text("FAIR")
            .fontColor("#ffffff")
            .fontSize(14)
            .fontStyle("bold")
            .rotation(345)
            .position("inside")
            .offsetY("95%")
            .offsetX(-15)
            .anchor("center")

        circularGauge.label(3)
            .text("GOOD")
            .fontColor("#ffffff")
            .fontSize(14)
            .fontStyle("bold")
            .rotation(15)
            .position("inside")
            .offsetY("95%")
            .offsetX(15)
            .anchor("center")

        circularGauge.label(4)
            .text(singleStar)
            .fontSize(10)
            .fontStyle("bold")
            .rotation(15)
            .position("inside")
            .offsetY("95%")
            .offsetX(40)
            .anchor("center")


        circularGauge.label(5)
            .text(doubleStar)
            .fontSize(10)
            .fontStyle("bold")
            .rotation(332)
            .position("inside")
            .offsetY("95%")
            .offsetX(60)
            .anchor("center")

        circularGauge.label(6)
            .text(tripleStar)
            .fontSize(10)
            .fontStyle("bold")
            .rotation(347)
            .position("inside")
            .offsetY("95%")
            .offsetX(80)
            .anchor("center")

        //This label is used to show custom range indication value

        circularGauge.label(7)
            .text("0")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(270)
            .position("outside")
            .offsetY("122%")
            .offsetX(-88)
            .anchor("center")

        circularGauge.label(8)
            .text("25")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(300)
            .position("outside")
            .offsetY("122%")
            .offsetX(-60)
            .anchor("center")

        circularGauge.label(9)
            .text("50")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(330)
            .position("outside")
            .offsetY("122%")
            .offsetX(-30)
            .anchor("center")

        circularGauge.label(10)
            .text("75")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(360)
            .position("outside")
            .offsetY("122%")
            .offsetX(0)
            .anchor("center")

        circularGauge.label(11)
            .text("100")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(30)
            .position("outside")
            .offsetY("122%")
            .offsetX(30)
            .anchor("center")

        circularGauge.label(12)
            .text("200")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(50)
            .position("outside")
            .offsetY("122%")
            .offsetX(50)
            .anchor("center")

        circularGauge.label(13)
            .text("300")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(70)
            .position("outside")
            .offsetY("122%")
            .offsetX(70)
            .anchor("center")

        circularGauge.label(14)
            .text("400")
            .fontColor("#5E5E5E")
            .fontSize(12)
            .fontStyle("bold")
            .rotation(90)
            .position("outside")
            .offsetY("122%")
            .offsetX(85)
            .anchor("center")


        binding.anyChart.setChart(circularGauge)

    }

    override fun onServiceReceived(model: FitModel) {
        AppPreferences.steps_count=model.steps
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())

        val updateData = SendStepsUpdates(1, 0, currentDate, 0, model.steps.toString())
        viewModel.sendSteps(updateData)
        updateSteps(model)
    }

    fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }


}

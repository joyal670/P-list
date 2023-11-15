package com.iroid.healthdomain.ui.home.my_health.history_and_activity.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.iroid.healthdomain.data.dummyModel.FitModel
import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.ActivityFragmentBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.fit.ActivityTracker
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.utils.visible
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit


class ActivityFragment :
    BaseFragment<ActivityViewModel, ActivityFragmentBinding, MyHealthRepository>(),
    ActivityTracker.onBroadCast {

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
        .build()
    private lateinit var model: FitModel
    private val TAG = "ActivityFragment"
    private lateinit var kProgressHUD: KProgressHUD

    companion object {
        fun newInstance() = ActivityFragment()
    }


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFragmentBinding {
        return ActivityFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ActivityViewModel> {
        return ActivityViewModel::class.java
    }

    override fun getFragmentRepository(): MyHealthRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        println("Token : ${token}")
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return MyHealthRepository(api, userPreferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.progressbar.visible(true)
        kProgressHUD=KProgressHUD.create(requireContext())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel("Fetching steps.....")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();

        ActivityTracker.onClickReceived(this)

        viewModel.stepsLiveData.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onActivityCreated: steps -> $it")
            binding.progressbar.visible(false)
            binding.viewModel = it
            val sdf = SimpleDateFormat("yyyy-M-dd")
            val currentDate = sdf.format(Date())

            val updateData = SendStepsUpdates(1, 0, currentDate, 30, it.steps.toString())
            viewModel.sendSteps(updateData)
            // viewModel.sendSteps(it.steps.toString(),currentDate)
        })

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
                    //  handleApiError(requireView(), it)
                }
            }
        }
    }


    override fun onServiceReceived(model: FitModel) {
        viewModel.setSteps(model)
        kProgressHUD.dismiss()
        binding.progressbar.visible(false)
        binding.viewModel = model
    }

    override fun onResume() {
        super.onResume()
        readData()
    }
    private fun readData(): String? {

        var steps: String? = null


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
            val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
            Log.e("#123456","$startTime")
            Log.e("#555555","$endTime")



            val dataSource = DataSource.Builder()
                .setAppPackageName("com.google.android.gms")
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .build()

            val request = DataReadRequest.Builder()
                .aggregate(dataSource)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()


            Fitness.getHistoryClient(requireActivity(), requireActivity().let { GoogleSignIn.getAccountForExtension(it, fitnessOptions) })
                .readData(request)
                .addOnSuccessListener { response ->
                    Log.e("123456", "readData:${response.buckets} ", )
                    val totalSteps = response.buckets
                        .flatMap { it.dataSets }
                        .flatMap { it.dataPoints }
                        .sumBy { it.getValue(Field.FIELD_STEPS).asInt() }
                    steps = totalSteps.toString()

                    AppPreferences.steps_count =totalSteps.toString()
                    model = FitModel(steps = totalSteps.toString())
                    onServiceReceived(model)

                }
                .addOnFailureListener {

                }




        }


//        Fitness.getHistoryClient(applicationContext, getGoogleAccount())
//                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//                .addOnSuccessListener { dataSet ->
//                    val total = when {
//                        dataSet.isEmpty -> 0
//                        else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
//                    }
//                    Log.i(TAG, "Total steps: $total")
//
////                    steps = total.toString()
////                    model = FitModel(steps = total.toString())
////                    //Toast.makeText(this, "$value", Toast.LENGTH_SHORT).show()
////                    viewModel.setSteps(model)
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "There was a problem getting the step count.", e)
//                }
        return steps
    }
}

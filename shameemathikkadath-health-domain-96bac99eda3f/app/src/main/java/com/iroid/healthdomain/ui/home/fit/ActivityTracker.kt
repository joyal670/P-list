package com.iroid.healthdomain.ui.home.fit

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.iroid.healthdomain.data.dummyModel.FitModel
import com.iroid.healthdomain.data.network.RemoteDataSource
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.preference.AppPreferences.steps_count
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class ActivityTracker : BroadcastReceiver() {


    companion object {
        private var applicationContext: Context? = null

        private var listener: onBroadCast? = null

        private const val TAG = "ActivityTracker"

        fun onClickReceived(broadCast: onBroadCast) {
            listener = broadCast
        }

    }

    lateinit var activity: Activity
    protected val remoteDataSource: RemoteDataSource by lazy { RemoteDataSource() }
    protected lateinit var userPreferences: UserPreferences

    private val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED)
            .build()


    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    lateinit var model: FitModel


    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null) {
            applicationContext = context
            readData()

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           // getCaloryData()
        }

    }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
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

            
            Fitness.getHistoryClient(applicationContext, applicationContext?.let { GoogleSignIn.getAccountForExtension(it, fitnessOptions) })
                    .readData(request)
                    .addOnSuccessListener { response ->
                        Log.e("123456", "readData:${response.buckets} ", )
                        val totalSteps = response.buckets
                                .flatMap { it.dataSets }
                                .flatMap { it.dataPoints }
                                .sumBy { it.getValue(Field.FIELD_STEPS).asInt() }
                        Log.i(TAG, "Total Time steps: $totalSteps")
                        steps = totalSteps.toString()

                        steps_count=totalSteps.toString()
                        model = FitModel(steps = totalSteps.toString())

                        listener?.let { it.onServiceReceived(model) }
                    }
                .addOnFailureListener {
                    Log.i(TAG, "readData123: ${it}")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCaloryData() {

        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusMinutes(30)
        Log.i(TAG, "Range Start: $startTime")
        Log.i(TAG, "Range End: $endTime")


        val readRequest = DataReadRequest.Builder()
                .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByActivityType(1, TimeUnit.SECONDS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        Fitness.getHistoryClient(applicationContext, applicationContext?.let { GoogleSignIn.getAccountForExtension(it, fitnessOptions) })
                .readData(readRequest)
                .addOnSuccessListener { response ->
                    // The aggregate query puts datasets into buckets, so flatten into a single list of datasets
                    for (dataSet in response.buckets.flatMap { it.dataSets }) {
                        if (dataSet.dataType.name.equals("com.google.calories.expended")){
                            dumpDataSet(dataSet)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG,"There was an error reading data from Google Fit", e)
                }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dumpDataSet(dataSet: DataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i(TAG,"Data point:")
            Log.i(TAG,"\tType: ${dp.dataType.name}")
            Log.i(TAG,"\tStart: ${dp.getStartTimeString()}")
            Log.i(TAG,"\tEnd: ${dp.getEndTimeString()}")
            for (field in dp.dataType.fields) {
                Log.i(TAG,"\tField: ${field.name} Value: ${dp.getValue(field)}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime().toString()


    private fun unSubscribe() {
        Fitness.getRecordingClient(applicationContext, applicationContext?.let { GoogleSignIn.getAccountForExtension(it, fitnessOptions) })
                // This example shows unsubscribing from a DataType. A DataSource should be used where the
                // subscription was to a DataSource. Alternatively, a Subscription object can be used.
                .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener {
                    Log.i(TAG, "Successfully unsubscribed.")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Failed to unsubscribe.")
                }
    }


    interface onBroadCast {
        fun onServiceReceived(model: FitModel)

    }

}

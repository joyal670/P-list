package com.iroid.healthdomain.ui.home.fit

import android.content.Context

class FitSingleton constructor(private val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: FitSingleton? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FitSingleton(context).also {
                        INSTANCE = it
                    }
                }
    }
//
//    private fun performActionForRequestCode(requestCode: FitActionRequestCode) = when (requestCode) {
//        FitActionRequestCode.READ_DATA -> readData()
//        FitActionRequestCode.SUBSCRIBE -> subscribe()
//    }

//    private fun subscribe() {
//        // To create a subscription, invoke the Recording API. As soon as the subscription is
//        // active, fitness data will start recording.
//        Fitness.getRecordingClient(context, getGoogleAccount())
//                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.i(TAG, "Successfully subscribed!")
//                        performActionForRequestCode(FitActionRequestCode.READ_DATA)
//
//                    } else {
//                        Log.w(TAG, "There was a problem subscribing.", task.exception)
//                    }
//                }
//    }
//
//    /**
//     * Reads the current daily step total, computed from midnight of the current day on the device's
//     * current timezone.
//     */
//    private fun readData() {
//
//        Fitness.getHistoryClient(context, getGoogleAccount())
//                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//                .addOnSuccessListener { dataSet ->
//                    val total = when {
//                        dataSet.isEmpty -> 0
//                        else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
//                    }
//                    Log.i(TAG, "Total steps: $total")
////                    model = FitModel(steps = total.toString())
////                    //Toast.makeText(requireContext(), "$value", Toast.LENGTH_SHORT).show()
////                    viewModel.setSteps(model)
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "There was a problem getting the step count.", e)
//                }
//    }


}

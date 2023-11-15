package com.iroid.healthdomain.ui.home.fit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessActivities
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResponse
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActivityFitBinding
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.mainActivity.HomeViewModel
import java.util.*
import java.util.concurrent.TimeUnit


const val TAG = "BasicSessions"
const val SAMPLE_SESSION_NAME = "Afternoon run"


enum class FitActionRequestCode {
    INSERT_AND_VERIFY_SESSION,
    DELETE_SESSION,
    SUBSCRIBE,
    CANCEL_SUBSCRIPTION,
    DUMP_SUBSCRIPTIONS
}


class FitActivity : BaseActivity<HomeViewModel, ActivityFitBinding,HomeRepository>() {

    companion object {
        private const val TAG = "FitActivity"
    }

    private val fitnessOptions: FitnessOptions by lazy {
        FitnessOptions.builder()
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_WRITE)
                .build()
    }

    val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    override fun getLayoutRes(): Int = R.layout.activity_fit

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionsAndRun(FitActionRequestCode.INSERT_AND_VERIFY_SESSION)
    }

    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            requestRuntimePermissions(fitActionRequestCode)
        }
    }

    private fun permissionApproved(): Boolean {
        val approved = if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            true
        }
        return approved
    }

    private fun fitSignIn(requestCode: FitActionRequestCode) {
        if (oAuthPermissionsApproved()) {
            performActionForRequestCode(requestCode)
            performActionForRequestCode(FitActionRequestCode.SUBSCRIBE)
        } else {
            requestCode.let {
                GoogleSignIn.requestPermissions(
                        this,
                        it.ordinal,
                        getGoogleAccount(), fitnessOptions)
            }
        }
    }

    private fun oAuthPermissionsApproved() = GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    private fun performActionForRequestCode(requestCode: FitActionRequestCode) = when (requestCode) {
        FitActionRequestCode.INSERT_AND_VERIFY_SESSION -> insertAndVerifySession()
        FitActionRequestCode.DELETE_SESSION -> deleteSession()

        FitActionRequestCode.SUBSCRIBE -> subscribe()
        FitActionRequestCode.CANCEL_SUBSCRIPTION -> cancelSubscription()
        FitActionRequestCode.DUMP_SUBSCRIPTIONS -> dumpSubscriptionsList()
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    private fun insertAndVerifySession() = insertSession().continueWith { verifySession() }

    private fun insertSession(): Task<Void> {
        // First, create a new session and an insertion request.
        val insertRequest = createSessionInsertRequest()

        // [START insert_session]
        Log.i(TAG, "Inserting the session in the Sessions API")
        return Fitness.getSessionsClient(this, getGoogleAccount())
                .insertSession(insertRequest)
                .addOnSuccessListener {
                    // At this point, the session has been inserted and can be read.
                    Log.i(TAG, "Session insert was successful!")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Log.i(TAG, "There was a problem inserting the session: $it")
                }
        // [END insert_session]
    }

    private fun createSessionInsertRequest(): SessionInsertRequest {
        Log.i(TAG, "Creating a new session for an afternoon run")
        // Setting start and end times for our run.
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        val now = Date()
        cal.time = now
        // Set a range of the run, using a start time of 30 minutes before this moment,
        // with a 10-minute walk in the middle.
        val endTime = cal.timeInMillis
        cal.add(Calendar.MINUTE, -10)
        val endWalkTime = cal.timeInMillis
        cal.add(Calendar.MINUTE, -10)
        val startWalkTime = cal.timeInMillis
        cal.add(Calendar.MINUTE, -10)
        val startTime = cal.timeInMillis

        // Create a data source
        val speedDataSource = DataSource.Builder()
                .setAppPackageName(this.packageName)
                .setDataType(DataType.TYPE_SPEED)
                .setStreamName("$SAMPLE_SESSION_NAME-speed")
                .setType(DataSource.TYPE_RAW)
                .build()

        val runSpeedMps = 10f
        val walkSpeedMps = 3f

        val firstRunSpeed = DataPoint.builder(speedDataSource)
                .setTimeInterval(startTime, startWalkTime, TimeUnit.MILLISECONDS)
                .setField(Field.FIELD_SPEED, runSpeedMps)
                .build()

        val walkSpeed = DataPoint.builder(speedDataSource)
                .setTimeInterval(startWalkTime, endWalkTime, TimeUnit.MILLISECONDS)
                .setField(Field.FIELD_SPEED, walkSpeedMps)
                .build()

        val secondRunSpeed = DataPoint.builder(speedDataSource)
                .setTimeInterval(endWalkTime, endTime, TimeUnit.MILLISECONDS)
                .setField(Field.FIELD_SPEED, runSpeedMps)
                .build()

        // Create a data set of the run speeds to include in the session.
        val speedDataSet = DataSet.builder(speedDataSource)
                .addAll(listOf(firstRunSpeed, walkSpeed, secondRunSpeed))
                .build()


        // [START build_insert_session_request_with_activity_segments]
        // Create a second DataSet of ActivitySegments to indicate the runner took a 10-minute walk
        // in the middle of the run.
        val activitySegmentDataSource = DataSource.Builder()
                .setAppPackageName(this.packageName)
                .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .setStreamName("$SAMPLE_SESSION_NAME-activity segments")
                .setType(DataSource.TYPE_RAW)
                .build()

        val firstRunningDp = DataPoint.builder(activitySegmentDataSource)
                .setTimeInterval(startTime, startWalkTime, TimeUnit.MILLISECONDS)
                .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.RUNNING)
                .build()

        val walkingDp = DataPoint.builder(activitySegmentDataSource)
                .setTimeInterval(startWalkTime, endWalkTime, TimeUnit.MILLISECONDS)
                .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.WALKING)
                .build()

        val secondRunningDp = DataPoint.builder(activitySegmentDataSource)
                .setTimeInterval(endWalkTime, endTime, TimeUnit.MILLISECONDS)
                .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.RUNNING)
                .build()
        val activitySegments = DataSet.builder(activitySegmentDataSource)
                .addAll(listOf(firstRunningDp, walkingDp, secondRunningDp))
                .build()


        // [START build_insert_session_request]
        // Create a session with metadata about the activity.
        val session = Session.Builder()
                .setName(SAMPLE_SESSION_NAME)
                .setDescription("Long run around Shoreline Park")
                .setIdentifier("UniqueIdentifierHere")
                .setActivity(FitnessActivities.RUNNING)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .build()

        // Build a session insert request
        // [END build_insert_session_request]
        // [END build_insert_session_request_with_activity_segments]

        return SessionInsertRequest.Builder()
                .setSession(session)
                .addDataSet(speedDataSet)
                .addDataSet(activitySegments)
                .build()
    }


    private fun verifySession(): Task<SessionReadResponse> {
        // Begin by creating the query.
        val readRequest = readFitnessSession()

        // [START read_session]
        // Invoke the Sessions API to fetch the session with the query and wait for the result
        // of the read request. Note: Fitness.SessionsApi.readSession() requires the
        // ACCESS_FINE_LOCATION permission.
        return Fitness.getSessionsClient(this, getGoogleAccount())
                .readSession(readRequest)
                .addOnSuccessListener { sessionReadResponse ->
                    // Get a list of the sessions that match the criteria to check the result.
                    val sessions = sessionReadResponse.sessions
                    Log.i(TAG, "Session read was successful. Number of returned sessions is: " + sessions.size)

                    for (session in sessions) {
                        // Process the session
                        dumpSession(session)

                        // Process the data sets for this session
                        val dataSets = sessionReadResponse.getDataSet(session)
                        for (dataSet in dataSets) {
                            dumpDataSet(dataSet)
                        }
                    }
                }
                .addOnFailureListener { Log.i(TAG, "Failed to read session") }
        // [END read_session]
    }

    private fun readFitnessSession(): SessionReadRequest {
        Log.i(TAG, "Reading History API results for session: $SAMPLE_SESSION_NAME")
        // Set a start and end time for our query, using a start time of 1 week before this moment.
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        val now = Date()
        cal.time = now
        val endTime = cal.timeInMillis
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        val startTime = cal.timeInMillis

        // Build a session read request
        // [START build_read_session_request]
        val sessionRequest = SessionReadRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .read(DataType.TYPE_SPEED)
                .setSessionName(SAMPLE_SESSION_NAME)
                .build()
        // [END build_read_session_request]
        return sessionRequest
    }


    private fun dumpSession(session: Session) {
        Log.i(TAG, "Data returned for Session: " + session.name
                + "\n\tDescription: " + session.description
                + "\n\tStart: " + session.getStartTime(TimeUnit.MILLISECONDS)
                + "\n\tEnd: " + session.getEndTime(TimeUnit.MILLISECONDS))
    }


    private fun dumpDataSet(dataSet: DataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")

        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            Log.i(TAG, "\tType: ${dp.dataType.name}")
            Log.i(TAG, "\tStart: ${dp.getStartTime(TimeUnit.MILLISECONDS)}")
            Log.i(TAG, "\tEnd: ${dp.getEndTime(TimeUnit.MILLISECONDS)}")
            dp.dataType.fields.forEach {
                Log.i(TAG, "\tField: ${it.name} Value: ${dp.getValue(it)}")

                        //calculateCalorie(dp.getValue(it))
            }
        }
    }


    private fun deleteSession() {
        Log.i(TAG, "Deleting today's session data for speed")

        // Set a start and end time for our data, using a start time of 1 day before this moment.
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        val now = Date()
        cal.time = now
        val endTime = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = cal.timeInMillis

        // Create a delete request object, providing a data type and a time interval
        val request = DataDeleteRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .addDataType(DataType.TYPE_SPEED)
                .deleteAllSessions() // Or specify a particular session here
                .build()

        // Delete request using HistoryClient and specify listeners that will check the result.
        Fitness.getHistoryClient(this, getGoogleAccount())
                .deleteData(request)
                .addOnSuccessListener { Log.i(TAG, "Successfully deleted today's sessions") }
                .addOnFailureListener {
                    // The deletion will fail if the requesting app tries to delete data
                    // that it did not insert.
                    Log.i(TAG, "Failed to delete today's sessions")
                }
    }


    private fun requestRuntimePermissions(requestCode: FitActionRequestCode) {
        val shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        requestCode.let {
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.")
                Snackbar.make(
                        binding.root,
                        "permission_rationale",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("ok") {
                            // Request permission
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    requestCode.ordinal)
                        }
                        .show()
            } else {
                Log.i(TAG, "Requesting permission")
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        requestCode.ordinal)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        performActionForRequestCode(FitActionRequestCode.DELETE_SESSION)
        performActionForRequestCode(FitActionRequestCode.CANCEL_SUBSCRIPTION)
    }



    private fun subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        // [START subscribe_to_datatype]
        Fitness.getRecordingClient(this, getGoogleAccount())
                .subscribe(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener { Log.i(TAG, "Successfully subscribed!")
                    performActionForRequestCode(FitActionRequestCode.DUMP_SUBSCRIPTIONS)
                }
                .addOnFailureListener { Log.i(TAG, "There was a problem subscribing.")
                }
        // [END subscribe_to_datatype]
    }

    private fun dumpSubscriptionsList() {
        // [START list_current_subscriptions]
        Fitness.getRecordingClient(this, getGoogleAccount())
                .listSubscriptions(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener { subscriptions ->
                    for (subscription in subscriptions) {
                        val dataType = subscription.dataType!!
                        Log.i(TAG, "Active subscription for data type: ${dataType.name}")
                    }

                    if (subscriptions.isEmpty()) {
                        Log.i(TAG, "No active subscriptions")
                    }
                }
        // [END list_current_subscriptions]
    }

    private fun cancelSubscription() {
        val dataTypeStr = DataType.TYPE_CALORIES_EXPENDED.toString()
        Log.i(TAG, "Unsubscribing from data type: $dataTypeStr")

        // Invoke the Recording API to unsubscribe from the data type and specify a callback that
        // will check the result.
        // [START unsubscribe_from_datatype]
        Fitness.getRecordingClient(this, getGoogleAccount())
                .unsubscribe(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener {
                    Log.i(TAG, "Successfully unsubscribed for data type: $dataTypeStr")
                }
                .addOnFailureListener {
                    // Subscription not removed
                    Log.i(TAG, "Failed to unsubscribe for data type: $dataTypeStr")
                }
        // [END unsubscribe_from_datatype]
    }
    override fun init() {

    }

    override fun getFragmentRepository(): HomeRepository {
        return HomeRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)
    }

}
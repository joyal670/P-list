package com.property.propertyuser.ui.startup.auth.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentSignupBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.ui.startup.auth.AuthActivity
import com.property.propertyuser.ui.startup.auth.login.LoginFragment
import com.property.propertyuser.ui.startup.auth.otp.OtpActivity
import com.property.propertyuser.utils.Constants.INTENT_MOBILE
import com.property.propertyuser.utils.Constants.INTENT_OTP
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : BaseFragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var activityListener: ActivityListener
    private lateinit var signUpViewModal: SignUpViewModal
    private var premissionGranded: Boolean = false
    private var placesClient: PlacesClient? = null
    private val PLACE_PICKER_REQUEST = 3
    private var passLng = 0.0
    private var passLat = 0.0
    private val RC_SIGN_IN = 11
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_signup, container, false)*/
    }

    override fun initData() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        callbackManager = CallbackManager.Factory.create()
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("TAG", "Success Login")
                getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
            }

            override fun onCancel() {
                Toast.makeText(requireContext(), "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Log.e("oncancel", Gson().toJson(exception.message))
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = context as AuthActivity
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("placedata", Gson().toJson(place))
                Log.e("place details", "Place: " + place.name + ", " + place.id)
                passLat = place.latLng!!.latitude
                passLng = place.latLng!!.longitude
                etLocation.text = place.name

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onstatus error", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {
        var facebookId = ""
        var faceBookName = ""
        var faceBookEmail = ""
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject
                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject.has("id")) {
                    facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId)
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }

                // Facebook Name
                if (jsonObject.has("name")) {
                    faceBookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", faceBookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    faceBookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", faceBookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }
                signUpViewModal.fetchGoogleLogin(
                    faceBookName, faceBookEmail, "",
                    "", facebookId, "", "2"
                )
            }).executeAsync()
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.e("id", account.id)
                Log.e("name", account.displayName)
                Log.e("email", account.email)
                signUpViewModal.fetchGoogleLogin(
                    account.displayName, account.email, "",
                    account.id, "", "", "1"
                )
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(requireContext(), getString(R.string.sign_in_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        signUpViewModal = SignUpViewModal()
    }

    override fun setupObserver() {
        signUpViewModal.getSignUpData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    val intent = Intent(requireContext(), OtpActivity::class.java)
                    intent.putExtra(INTENT_OTP, it.data!!.otp.toString())
                    intent.putExtra(INTENT_MOBILE, etUserMobile.text.toString())
                    startActivity(intent)
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.something_wrong),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.no_internet),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
        signUpViewModal.getGoogleLoginData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    AppPreferences.isLogin = true
                    AppPreferences.token = it.data!!.data.api_token
                    if (it.data.data.name.isNotBlank()) {
                        AppPreferences.user_name = it.data.data.name
                    }
                    if (it.data.data.email.isNotBlank()) {
                        AppPreferences.user_email = it.data.data.email
                    }
                    if (it.data.data.profile_pic.isNotBlank()) {
                        AppPreferences.user_profile_image = it.data.data.profile_pic
                    }

                    mGoogleSignInClient.signOut()
                    startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                    requireActivity().finish()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.something_wrong),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.no_internet),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnRegisterWithFB.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }
        btnRegisterWithGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding.tvSignIn.setOnClickListener {
            activityListener.navigateToFragment(LoginFragment())
        }
        binding.btnRegister.setOnClickListener {
            if (isValidate()) {
                signUpViewModal.addSignUp(
                    binding.etUserMobile.text.toString().trim(),
                    binding.etUserEmail.text.toString().trim(),
                    binding.etUserName.text.toString().trim(),
                    passLat.toString(),
                    passLng.toString(),
                    binding.etLocation.text.toString().trim(),
                    binding.etReferralCode.text.trim().toString()
                )
            }

        }
        binding.etLocation.setOnClickListener {
            getLocationDetails()
        }
    }

    private fun getLocationDetails() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        premissionGranded = true
        val fields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(requireContext())
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }

    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build()
                    .send { result1 ->
                        if (result1.allGranted()) {
                            getLocationDetails()
                        }

                    }
            }
            if (result.allShouldShowRationale()) {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.allow_location_permission),
                    Toaster.State.WARNING,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    private fun isValidate(): Boolean {
        when {
            binding.etUserName.text.toString().trim().isEmpty() -> {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.name_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
                return false
            }
            binding.etUserEmail.text.toString().trim().isEmpty() -> {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.email_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
                return false
            }
            binding.etUserMobile.text.toString().trim().isEmpty() -> {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.mobile_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
                return false
            }
            binding.etLocation.text.toString().trim().isEmpty() -> {
                if (!premissionGranded) {
                    getLocationDetails()
                }
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.location_details_needed),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
                return false
            }
            else -> {
                return true
            }
        }

    }
}
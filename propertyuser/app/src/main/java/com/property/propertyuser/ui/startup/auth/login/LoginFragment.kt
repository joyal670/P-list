package com.property.propertyuser.ui.startup.auth.login

/*import com.property.propertyuser.data.api.ApiHelperImpl
import com.property.propertyuser.data.api.RetrofitBuilder*/
/*import com.property.propertyuser.utils.ViewModelFactory*/
import android.Manifest
import android.annotation.SuppressLint
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentLoginBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.ui.startup.auth.AuthActivity
import com.property.propertyuser.ui.startup.auth.otp.OtpActivity
import com.property.propertyuser.ui.startup.auth.signup.SignupFragment
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected


class LoginFragment : BaseFragment() {
    // declare a global variable of FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var activityListener: ActivityListener
    private lateinit var loginViewModel: LoginViewModal
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 11
    var premissionGranded: Boolean = false
    var lat: String = ""
    var lng: String = ""
    private lateinit var callbackManager: CallbackManager

    // private val customProgressDialog = CustomProgressDialog()
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /* return inflater?.inflate(R.layout.fragment_login, container, false)*/
    }

    private fun getLastKnownLocation() {
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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.e("fusedLocationClient", Gson().toJson(location))
                    lat = location.latitude.toString()
                    lng = location.longitude.toString()
                    premissionGranded = true
                    // use your location object
                    // get latitude , longitude and other info from this
                }

            }

    }

    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build()
                    .send { result1 ->
                        if (result1.allGranted()) {
                            getLastKnownLocation()
                        }

                    }
            }
            if (result.allShouldShowRationale()) {
                Toaster.showToast(
                    requireContext(),
                    "Please allow permissions",
                    Toaster.State.WARNING,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun initData() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        callbackManager = CallbackManager.Factory.create()
        getLastKnownLocation()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                loginViewModel.fetchGoogleLogin(
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
                loginViewModel.fetchGoogleLogin(
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = context as AuthActivity
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        loginViewModel = LoginViewModal()
    }

    override fun setupObserver() {
        loginViewModel.getLoginData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    val intent = Intent(requireContext(), OtpActivity::class.java)
                    intent.putExtra(Constants.INTENT_OTP, it.data!!.otp.toString())
                    intent.putExtra(
                        Constants.INTENT_MOBILE,
                        binding.etMobileNumberLogin.text.toString()
                    )
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
        loginViewModel.getGoogleLoginData().observe(this, Observer {
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
        //            if(premissionGranded){
//                if(!binding.etMobileNumberLogin.text.toString().trim().isNullOrEmpty()){
//                    loginViewModel.login(binding.etMobileNumberLogin.toString().trim())
//                }
//            }else{
//                Toaster.showToast(requireContext(),"Mobile number is required",Toaster.State.ERROR,Toast.LENGTH_LONG)
//            }
        binding.btnLogin.setOnClickListener {

            if (!binding.etMobileNumberLogin.text.toString().trim().isNullOrEmpty()) {
                loginViewModel.login(binding.etMobileNumberLogin.text.toString().trim())
            } else {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.mobile_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
        }
        binding.tvSignUp.setOnClickListener {
            activityListener.navigateToFragment(SignupFragment())
        }
        binding.btnLoginWithGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding.btnLoginWithFB.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }
    }
}
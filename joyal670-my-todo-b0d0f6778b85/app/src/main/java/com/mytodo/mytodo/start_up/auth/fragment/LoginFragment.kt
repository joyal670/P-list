package com.mytodo.mytodo.start_up.auth.fragment

import com.facebook.appevents.AppEventsLogger;
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentLoginBinding
import com.mytodo.mytodo.ui.main.home.activity.DashboardActivity
import com.mytodo.mytodo.utils.AppPreferences.prefIsLogin
import com.mytodo.mytodo.utils.AppPreferences.prefUserID
import com.mytodo.mytodo.utils.AppPreferences.prefUserDisplayName
import com.mytodo.mytodo.utils.AppPreferences.prefUserEmail
import com.mytodo.mytodo.utils.AppPreferences.prefUserProfilePic
import java.util.*


class LoginFragment : BaseFragment()
{
    private lateinit var binding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123

    private lateinit var callbackManager: CallbackManager

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater!!,container,false)
        return binding.root
    }

    override fun initData() {
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        googleSignInClient.signOut().addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                FirebaseAuth.getInstance().signOut()
            }
        })

        binding.ivFacebook.setFragment(this)
        FacebookSdk.sdkInitialize(requireContext())
        callbackManager = CallbackManager.Factory.create()
        binding.ivFacebook.setReadPermissions("email", "public_profile")
        binding.ivFacebook.registerCallback(callbackManager,object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.e("TAG", "facebook:onSuccess:$result")
                if (result != null) {
                    handleFacebookAccessToken(result.accessToken)
                }
            }

            override fun onCancel() {
                Log.e("TAG", "facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.e("TAG", "facebook:onError", error)
            }
        })

    }

    override fun setupUI() {
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        binding.ivGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
            }

       /* binding.ivFacebook.setOnClickListener {
            // fd03dd0b57cb280df517d75ee82a92af app secret
            //1840783956096090 app id
            //Bmce+9aHdOoVtE7fS3B07tfj7Bc= ssl

            LoginManager.getInstance().logInWithReadPermissions (requireActivity(), listOf("email","public_profile"))
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showProgress()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.e("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    dismissProgress()
                } else {
                    Log.e("TAG", "signInWithCredential:failure", task.exception)
                    dismissProgress()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?)
    {
        showProgress()
        prefUserID = user!!.uid
        prefUserDisplayName = user.displayName
        prefUserProfilePic = user.photoUrl.toString()
        prefUserEmail = user.email
        prefIsLogin = true
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        startActivity(intent)
        finishAffinity(requireActivity())
        dismissProgress()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.e("TAG", "handleFacebookAccessToken:$token")

        showProgress()
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.e("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    LoginManager.getInstance().logOut()
                    dismissProgress()
                } else {
                    Log.e("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    dismissProgress()
                }
            }
    }

}
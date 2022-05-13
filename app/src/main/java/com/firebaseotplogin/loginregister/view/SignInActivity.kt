package com.firebaseotplogin.loginregister.view


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.firebaseotplogin.R
import com.firebaseotplogin.app.MyApplication
import com.firebaseotplogin.base.activity.BaseDataBindingActivity
import com.firebaseotplogin.databinding.LoginActivityDataBinding
import com.firebaseotplogin.di.DaggerProvider
import com.firebaseotplogin.loginregister.callbacks.SignInActivityViewCallBacks
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*

class SignInActivity :
    BaseDataBindingActivity<LoginActivityDataBinding>(R.layout.login_activity_layout),
    SignInActivityViewCallBacks {


    lateinit var device_token: String
    private val TAG = SignInActivity::class.java.simpleName

    lateinit var auth: FirebaseAuth


    /**
     *  below code for google signin
     */
    var googleAuthType: String? = ""
    private var mfirebaseAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var RC_SIGN_IN_GOOGLE_SIGNIN = 200
    var accessToken = ""

    companion object {
        var mInstance: SignInActivity? = null

    }

    fun getInstance(): SignInActivity {
        return mInstance!!
    }


    override fun onDataBindingCreated() {
        mInstance = this
        binding.callback = this
        binding.lifecycleOwner = this
        supportActionBar!!.hide()


    }

    override fun injectDaggerComponent() {
        DaggerProvider.getAppComponent()?.inject(this)
    }




    /**
     *  below code for google signin
     */
     fun initGoogleIntegration(webClientId :String) {
        mfirebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE_SIGNIN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE_SIGNIN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            handleGoogleSignInResult(result!!)
        }
    }

     fun handleGoogleSignInResult(result: GoogleSignInResult) {
         var response: Any? = null


         if (result.isSuccess) {
            val account = result.signInAccount
            var idToken_google = account!!.idToken
            var name_google = account.displayName
            var email_google = account.email
            // image = account.getPhotoUrl().toString();



            val credential = GoogleAuthProvider.getCredential(idToken_google, null)


            mfirebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener(
                this,
                OnCompleteListener<AuthResult?> { task ->
                    Log.d(
                        "TAG",
                        "signInWithCredential:onComplete:" + task.isSuccessful
                    )
                    if (task.isSuccessful) {

                        Toast.makeText(this, "google signin successful", Toast.LENGTH_LONG).show();

                        mGoogleSignInClient!!.signOut()
                        val user = task.result!!.user!!

                        response = user


                    } else {
                        Log.w("TAG", "signInWithCredential" + task.exception!!.message)
                        task.exception!!.printStackTrace()
                        Toast.makeText(this, " Google Authentication failed : " + task.exception!!.message, Toast.LENGTH_SHORT).show()

                        response = task.exception!!.message.toString()

                    }
                })



        } else {
            // Google Sign In failed, update UI appropriately

            response = "Google Login Unsuccessful" + result.status.statusCode


            Toast.makeText(
                this,
                "Google Login Unsuccessful" + result.status.statusCode,
                Toast.LENGTH_LONG
            ).show();
        }
    }



}
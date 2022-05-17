package com.firebaseotplogin.loginregister.view

import android.util.Log
import com.firebaseotplogin.R
import com.firebaseotplogin.base.activity.BaseDataBindingActivity
import com.firebaseotplogin.databinding.VerifyOtpActivityDataBinding
import com.firebaseotplogin.di.DaggerProvider

import com.firebaseotplogin.loginregister.callbacks.VerifyOtpViewCallbacks
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

open class OtpActivity :
    BaseDataBindingActivity<VerifyOtpActivityDataBinding>(R.layout.otp_activity_layout),
    VerifyOtpViewCallbacks {


    private var db: FirebaseFirestore? = null

    /**
     *  below code for OTP signin
     */
    companion object {
        lateinit var mobile_with_code: String
        lateinit var mobile_no: String
        lateinit var country_code: String
        var firebaseUid: String? = ""
        var mInstance: OtpActivity? = null


    }

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private var mVerificationId: String? = ""
    private var FROM_WHICH_SCREEN: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val TAG = OtpActivity::class.java.simpleName


    fun getInstance(): OtpActivity {
        return mInstance!!
    }


    override fun injectDaggerComponent() {
        DaggerProvider.getAppComponent()?.inject(this)
    }

    override fun onDataBindingCreated() {
        mInstance = this
        binding.callback = this
        binding.lifecycleOwner = this
        supportActionBar!!.hide()
    }


    fun initOTPVerification() {

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance();


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }


            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG", "onVerificationFailed  $e")
                //  CommonUtilities.hideLoader()
                // CommonUtilities.showToast(this@OtpActivity, e.message)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                mVerificationId = verificationId
                resendToken = token

            }
        }

        sendVerificationCode(mobile_with_code)

    }


    fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG", "Auth started")

        //  CommonUtilities.showLoader(this)
    }


    fun verifyOTPAndSignInWithPhoneAuthCredential(code: String): Any? {

        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        var response: Any? = null


        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->


                    //  CommonUtilities.hideLoader()
                    if (task.isSuccessful) {

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            //   CommonUtilities.showToast(this, "Invalid OTP!" + (task.exception as FirebaseAuthInvalidCredentialsException).message)

                            response =
                                (task.exception as FirebaseAuthInvalidCredentialsException).message

                        } else {

                            var user = task.result.user

                            firebaseUid = user?.uid

                            response = user


                        }

                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {


                            response =
                                (task.exception as FirebaseAuthInvalidCredentialsException).message

                            /*   CommonUtilities.showToast(
                                   this,
                                   "Invalid OTP!" + (task.exception as FirebaseAuthInvalidCredentialsException).message
                               )*/
                        }
                    }
                })

        return response
    }
}






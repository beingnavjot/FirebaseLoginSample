package com.firebaseotplogin.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.firebaseotplogin.utils.Util

import javax.inject.Inject

/**
 * BaseFragment class that takes in
 * @param screenLayoutId layout resource ID for the fragment
 * and inflates the layout.
 */
abstract class BaseFragment(@LayoutRes private val screenLayoutId: Int) : Fragment() {
    lateinit var alertDialog: Dialog

    @Inject
    open lateinit var util: Util



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processArguments(arguments)
        injectDaggerComponent()
        registerBackPressListener()

    }

    abstract fun injectDaggerComponent()

    open fun processArguments(arguments: Bundle?) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        restoreInstanceState(savedInstanceState)
        return inflater.inflate(screenLayoutId, container, false)
    }

    open fun restoreInstanceState(savedInstanceState: Bundle?) {
    }

    protected fun show() {
        alertDialog.show()
    }

    protected fun dismiss() {
        alertDialog?.let {
            alertDialog.dismiss()
        }
    }
    // region Navigation
    /**
     * This function is called in the onCreate() of the Base fragment. This will enable the child fragments
     * to receive onBackPress callback and will be cleaner than routing it via the holding activity
     *
     * Ref: https://developer.android.com/guide/navigation/navigation-custom-back
     */
    private fun registerBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val consumedInChildFragment = onBackPress()
                    if (!consumedInChildFragment) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    /**
     * Override this method in your fragment if you want to receive an event when the user presses the back button
     * This method returns false by default. This means that if you dont override the method, we assume
     * that your fragment has not consumed any back press event and send's the event to the activity instead
     *
     * If override this function and return true in your fragment it means that the fragment has consumed
     * the back press and the holding activity will not need to handle it
     */
    open fun onBackPress(): Boolean = false
    // endregion Navigation
}
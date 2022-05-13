package com.firebaseotplogin.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * BaseFragment class that takes in
 * @param DataBindingClass that serves as the binding for the fragment
 * @param screenLayoutId layout resource ID for the fragment
 *
 * This should be used for classes that do not require a ViewModel, but still uses DataBinding to reference views.
 */
abstract class BaseDataBindingFragment<DataBindingClass : ViewDataBinding>(
    @LayoutRes private val screenLayoutId: Int
) : BaseFragment(
    screenLayoutId = screenLayoutId
) {

    protected open lateinit var binding: DataBindingClass
  //  private var viewModel: T? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        restoreInstanceState(savedInstanceState)
        initialiseDataBinding(inflater, container)
        return binding.root
    }

    // region DataBinding
    open fun initialiseDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, screenLayoutId, container, false)
        //binding.lifecycleOwner = this
        onDataBindingCreated()

    }
   // abstract fun getViewModel(): T?
    abstract fun onDataBindingCreated()
    // endregion DataBinding
}
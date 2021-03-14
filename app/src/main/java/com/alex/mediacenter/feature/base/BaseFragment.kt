package com.alex.mediacenter.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.alex.mediacenter.feature.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateView(inflater, container)

        setupView()
        setupViewBinding()
        setupViewModel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ----------------------------------------------------------------------------

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?): VB
    open fun setupView() {}
    open fun setupViewBinding() {}
    open fun setupViewModel() {}

    // ----------------------------------------------------------------------------

    fun addBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        (activity as MainActivity).addBottomSheetCallback(callback)
    }

    fun removeBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        (activity as MainActivity).removeBottomSheetCallback(callback)
    }

    fun setBottomSheetState(state: Int) {
        (activity as MainActivity).setBottomSheetState(state)
    }

    // ----------------------------------------------------------------------------

    /*
     * This extensions-function has build-in check for nullability.
     */
    fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }
}
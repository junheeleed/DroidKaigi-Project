package com.info.droidkaigiapplication.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindingFragment<T: ViewDataBinding>: Fragment() {

    private var _dataBinding: T? = null
    protected val dataBinding get() =  _dataBinding!!

    abstract fun getLayoutResId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _dataBinding = (DataBindingUtil.inflate(LayoutInflater.from(context),
                getLayoutResId(),
                container,
                false) as T)
        setDataBinding(_dataBinding!!)
        return _dataBinding!!.root
    }

    abstract fun setDataBinding(dataBinding: T)

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}
package com.info.droidkaigiapplication.presentation.more

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentMoreBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment

import com.info.droidkaigiapplication.presentation.openBrowser

class MoreFragment
    : DataBindingFragment<FragmentMoreBinding>(),
        MoreFragmentDataBindingImpl {

    override fun getLayoutResId() = R.layout.fragment_more

    override fun setDataBinding(dataBinding: FragmentMoreBinding) {
        with(dataBinding) {
            dataBindingImpl = this@MoreFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
    }

    private fun setActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(dataBinding.toolbar)
        dataBinding.toolbar.title = getString(R.string.app_name)
    }

    override fun openBrowser(urlString: String) {
       requireContext().openBrowser(urlString)
    }
}
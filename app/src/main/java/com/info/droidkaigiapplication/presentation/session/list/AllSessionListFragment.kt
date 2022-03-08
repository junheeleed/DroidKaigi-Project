package com.info.droidkaigiapplication.presentation.session.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentAllSessionListBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment
import com.info.droidkaigiapplication.presentation.getScrollState
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.session.SessionListLoadEvent
import com.info.droidkaigiapplication.presentation.session.SessionsFragment
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllSessionListFragment
    : DataBindingFragment<FragmentAllSessionListBinding>(),
        SessionsFragment.SavePreviousSessionScroller {

    private val sessionRecyclerViewPool: RecyclerView.RecycledViewPool by inject()
    private val viewModel: AllSessionListViewModel by viewModel()

    override fun getLayoutResId() = R.layout.fragment_all_session_list

    override fun setDataBinding(dataBinding: FragmentAllSessionListBinding) {
        with(dataBinding) {
            context = requireContext()
            vm = viewModel
            recyclerViewPool = sessionRecyclerViewPool
            lifecycleOwner = this@AllSessionListFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.loadAllSessionList()
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            EventBus.getDefault().post(SessionListLoadEvent(it))
        })
    }

    companion object {
        fun newInstance() = AllSessionListFragment()
    }

    override fun requestSavingScrollState() {
        val layoutManager = dataBinding.sessionRecyclerview.layoutManager as LinearLayoutManager
        PreviousSessionPrefs.scrollState = layoutManager.getScrollState()
    }
}
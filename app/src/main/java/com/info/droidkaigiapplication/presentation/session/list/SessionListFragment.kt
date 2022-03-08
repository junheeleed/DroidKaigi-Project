package com.info.droidkaigiapplication.presentation.session.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentSessionListBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment
import com.info.droidkaigiapplication.presentation.getScrollState
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.session.SessionListLoadEvent
import com.info.droidkaigiapplication.presentation.session.SessionsFragment
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionListFragment
    : DataBindingFragment<FragmentSessionListBinding>(),
        SessionsFragment.SavePreviousSessionScroller {

    private val sessionRecyclerViewPool: RecyclerView.RecycledViewPool by inject()
    private val viewModel: SessionListViewModel by viewModel()

    override fun getLayoutResId() = R.layout.fragment_session_list

    override fun setDataBinding(dataBinding: FragmentSessionListBinding) {
        with(dataBinding) {
            context = requireContext()
            vm = viewModel
            recyclerViewPool = sessionRecyclerViewPool
            lifecycleOwner = this@SessionListFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roomId = requireArguments().getInt(ROOM_ID_KEY, 0)
        setObservers()
        viewModel.loadSessionList(roomId)
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            EventBus.getDefault().post(SessionListLoadEvent(it))
        })
    }

    companion object {
        const val ROOM_ID_KEY = "RoomIdKey"
        fun newInstance(roomId: Int): SessionListFragment {
            return SessionListFragment().apply {
                arguments = bundleOf(ROOM_ID_KEY to roomId)
            }
        }
    }

    override fun requestSavingScrollState() {
        val layoutManager = dataBinding.sessionRecyclerview.layoutManager as LinearLayoutManager
        PreviousSessionPrefs.scrollState = layoutManager.getScrollState()
    }
}
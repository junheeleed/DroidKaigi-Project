package com.info.droidkaigiapplication.presentation.session.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentSessionDetailBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment
import kotlinx.android.synthetic.main.fragment_session_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.RuntimeException

class SessionDetailFragment
    : DataBindingFragment<FragmentSessionDetailBinding>() {

    private lateinit var listener: SessionDetailInteractionListener
    private val viewModel: SessionDetailViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SessionDetailInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement SessionDetailInteractionListener")
        }
    }

    override fun getLayoutResId() = R.layout.fragment_session_detail

    override fun setDataBinding(dataBinding: FragmentSessionDetailBinding) {
        with(dataBinding) {
            vm = viewModel
            lifecycleOwner = this@SessionDetailFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionId = requireArguments().getInt(SESSION_ID, 0)
        val roomId = requireArguments().getInt(ROOM_ID, 0)
        setActionBar()
        setListener()
        viewModel.loadSession(sessionId, roomId)
    }

    private fun setActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(dataBinding.toolbar)
    }

    private fun setListener() {
        dataBinding.appbarLayout.addOnOffsetChangedListener(
            AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout> {
                    appBarLayout, verticalOffset ->
                val factor = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
                dataBinding.toolbarTextColorFactor = factor
            }
        )
        dataBinding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        dataBinding.previous.setOnClickListener {
            listener.previousSession()
        }
        dataBinding.next.setOnClickListener {
            listener.nextSession()
        }
    }

    companion object {
        private const val SESSION_ID = "SessionId"
        private const val ROOM_ID = "RoomId"
        fun newInstance(sessionId: Int,
                        roomId: Int): SessionDetailFragment {
            return SessionDetailFragment().apply {
                arguments = bundleOf(SESSION_ID to sessionId, ROOM_ID to roomId)
            }
        }
    }

    interface SessionDetailInteractionListener {
        fun previousSession()
        fun nextSession()
    }
}
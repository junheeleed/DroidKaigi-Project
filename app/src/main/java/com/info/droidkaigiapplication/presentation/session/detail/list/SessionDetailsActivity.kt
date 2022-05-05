package com.info.droidkaigiapplication.presentation.session.detail.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.ActivitySessionDetailsBinding
import com.info.droidkaigiapplication.presentation.session.detail.SessionDetailFragment
import com.info.droidkaigiapplication.presentation.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionDetailsActivity
    : AppCompatActivity(), SessionDetailFragment.SessionDetailInteractionListener {

    private var _dataBinding: ActivitySessionDetailsBinding? = null
    private val dataBinding get() = _dataBinding!!

    private val viewModel: SessionDetailsViewModel by viewModel()
    private var sessionId = 0
    private var roomId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _dataBinding = (DataBindingUtil.setContentView(this, R.layout.activity_session_details))
        _dataBinding!!.lifecycleOwner = this
        init()
        viewModel.loadSessionList(sessionId, roomId)
    }

    private fun init() {
        sessionId = intent.getIntExtra(SESSION_ID, 0)
        roomId = intent.getIntExtra(ROOM_ID, 0)

        dataBinding.appCompatActivity = this
        dataBinding.vm = viewModel
        dataBinding.sessionId = sessionId
        dataBinding.roomId = roomId
    }

    override fun previousSession() {
        val currentPosition = dataBinding.viewpager.currentItem
        if (0 < currentPosition) {
            dataBinding.viewpager.currentItem = currentPosition - 1
        } else {
            showToast("there is no page")
        }
    }

    override fun nextSession() {
        val currentPosition = dataBinding.viewpager.currentItem
        if (viewModel.getSessionDetailsSize() > currentPosition) {
            dataBinding.viewpager.currentItem = currentPosition + 1
        } else {
            showToast("there is no page")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    companion object {
        const val SESSION_ID = "SessionId"
        const val ROOM_ID = "RoomId"
        fun Context.start(sessionId: Int) {
            startActivity(createIntent(sessionId))
        }

        fun Context.start(sessionId: Int, roomId: Int) {
            startActivity(createIntent(sessionId, roomId))
        }

        private fun Context.createIntent(sessionId: Int, roomId: Int = 0): Intent {
            return Intent(this, SessionDetailsActivity::class.java).apply {
                putExtra(SESSION_ID, sessionId)
                putExtra(ROOM_ID, roomId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _dataBinding = null
    }
}

class SessionDetailAdapter(
        activity: AppCompatActivity,
        private val roomId: Int
) : FragmentStateAdapter(activity) {

    var sessionDetailSummaries = listOf<SessionDetailSummary>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() {
            return field
        }

    override fun getItemCount(): Int {
        return sessionDetailSummaries.size
    }

    override fun createFragment(position: Int): Fragment {
        val sessionDetailSummaries = sessionDetailSummaries[position]
        return SessionDetailFragment.newInstance(
                sessionDetailSummaries.id,
                if (roomId > 0) sessionDetailSummaries.roomId
                else if ((sessionDetailSummaries.id > 0) and (roomId == 0)) 0
                else -1
        )
    }
}
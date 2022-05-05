package com.info.droidkaigiapplication.presentation.session.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.RoomRepository
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.repository.SpeakerRepository
import com.info.droidkaigiapplication.data.source.room.RoomData
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.presentation.*
import com.info.droidkaigiapplication.presentation.session.Room
import com.info.droidkaigiapplication.presentation.session.detail.model.*
import com.info.droidkaigiapplication.presentation.session.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionDetailViewModel(
        private val context: Context,
        private val roomRepository: RoomRepository,
        private val sessionRepository: SessionRepository,
        private val speakerRepository: SpeakerRepository)
    : ViewModel() {

    private val _isLoading: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _sessionDetail: NotNullMutableLiveData<SessionDetail> = NotNullMutableLiveData(SessionDetail.empty())
    val sessionDetail: LiveData<SessionDetail> get() = _sessionDetail

    private val _nextSessionTitle: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val nextSessionTitle: LiveData<String> get() = _nextSessionTitle

    private val _isFirst: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isFirst: LiveData<Boolean> get() = _isFirst

    private val _isLast: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLast: LiveData<Boolean> get() = _isLast

    private val _isNavigateNeeded: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isNavigateNeeded: LiveData<Boolean> get() = _isNavigateNeeded

    fun loadSession(sessionId: Int, roomId: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val roomsResult = roomRepository.getRooms()
            if (roomsResult.isFailed()) {
                postErrorMessage(roomsResult)
                return@launch
            }

            val speakersResult = speakerRepository.getSpeakers()
            if (speakersResult.isFailed()) {
                postErrorMessage(speakersResult)
                return@launch
            }

            val sessionsResult = sessionRepository.getSessions()
            if (sessionsResult.isFailed()) {
                postErrorMessage(sessionsResult)
                return@launch
            }

            val sessionDataList = sessionsResult.getSessionDataList(roomId, sessionId)
            withContext(Dispatchers.Main) {
                setSessionDetail(sessionId,
                        speakersResult.getData(),
                        roomsResult.getData(),
                        sessionDataList)
                setNavigateNeeded(sessionId, roomId)
                _isLoading.postValue(false)
            }
        }
    }

    private fun setSessionDetail(sessionId: Int,
                                 speakerDateList: List<SpeakerData>,
                                 roomDateList: List<RoomData>,
                                 sessionsDateList: List<SessionData>) {
        val position = sessionsDateList.indexOfFirst { it.id == sessionId }
        if ((position > -1) and (position < sessionsDateList.size)) {
            val nextPageTitle = if (position == sessionsDateList.size - 1) "" else sessionsDateList[position + 1].title
            val session = sessionsDateList[position]
            val speakerDetail = speakerDateList.toSpeakerDetail(session.speakers.first())
            val room = roomDateList.toRoom(session.roomId)
            this@SessionDetailViewModel._sessionDetail.postValue(session.toSessionDetail(speakerDetail, room))
            if (sessionsDateList.first().id == sessionsDateList.last().id) {
                setFirstAndLast(true, true)
            } else if (session.id == sessionsDateList.first().id) {
                _nextSessionTitle.postValue(nextPageTitle)
                setFirstAndLast(true, false)
            } else if (session.id == sessionsDateList.last().id) {
                setFirstAndLast(false, true)
            } else {
                _nextSessionTitle.postValue(nextPageTitle)
                setFirstAndLast(false, false)
            }
        }
    }

    private fun List<SpeakerData>.toSpeakerDetail(speakerId: String): SpeakerDetail {
        return this.first { it.id == speakerId }.toSpeakerDetail()
    }

    private fun List<RoomData>.toRoom(roomId: Int): Room {
        return this.first { it.id == roomId }.toRoom()
    }

    private fun Result<List<SessionData>>.getSessionDataList(roomId: Int,
                                                             sessionId: Int): List<SessionData> {
        return this.getData().run {
            if (roomId > 0) {
                this.filter { it.roomId == roomId }
            } else if ((sessionId > 0) and (roomId == 0)) {
                this.filter { it.id == sessionId }
            } else {
                this
            }
        }
    }

    private fun setFirstAndLast(isFirst: Boolean, isLast: Boolean) {
        this@SessionDetailViewModel._isFirst.postValue(isFirst)
        this@SessionDetailViewModel._isLast.postValue(isLast)
    }

    private fun setNavigateNeeded(sessionId: Int, roomId: Int) {
        if ((sessionId > 0) and (roomId == 0)) {
            _isNavigateNeeded.postValue(false)
        } else if ((sessionId == 0) and (roomId == 0)) {
            _isNavigateNeeded.postValue(false)
        } else {
            _isNavigateNeeded.postValue(true)
        }
    }

    private suspend fun postErrorMessage(result: Result<Any>) {
        val errorMessage = result.getFailureMessage(context)
        withContext(Dispatchers.Main) {
            _errorMessage.postValue(errorMessage)
            _isLoading.postValue(false)
        }
    }
}
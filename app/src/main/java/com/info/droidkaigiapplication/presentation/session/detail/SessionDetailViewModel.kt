package com.info.droidkaigiapplication.presentation.session.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.repository.SpeakerRepository
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.presentation.NotNullMutableLiveData
import com.info.droidkaigiapplication.presentation.getFailureMessage
import com.info.droidkaigiapplication.presentation.intersect
import com.info.droidkaigiapplication.presentation.isFailed
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail
import com.info.droidkaigiapplication.presentation.session.detail.model.Speaker
import com.info.droidkaigiapplication.presentation.session.detail.model.toSessionDetail
import com.info.droidkaigiapplication.presentation.session.detail.model.toSpeakerList
import com.info.droidkaigiapplication.presentation.session.model.Session
import com.info.droidkaigiapplication.presentation.session.model.toSessionList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionDetailViewModel(
        private val context: Context,
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
            val speakersResult = speakerRepository.getSpeakers()
            if (speakersResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(speakersResult)
                    _isLoading.postValue(false)
                }
                return@launch
            }

            val sessionsResult = sessionRepository.getSessions()
            if (sessionsResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(sessionsResult)
                    _isLoading.postValue(false)
                }
                return@launch
            }
            val sessions = (sessionsResult as Result.Succeed<List<SessionData>>).data.toSessionList().run {
                if (roomId > 0) {
                    this.filter { it.roomId == roomId }
                } else if ((sessionId > 0) and (roomId == 0)) {
                    this.filter { it.id == sessionId }
                } else {
                    this
                }
            }

            withContext(Dispatchers.Main) {
                setSessionDetail(sessionId, speakersResult, sessions)
                setNavigateNeeded(sessionId, roomId)
                _isLoading.postValue(false)
            }
        }
    }

    private fun setSessionDetail(sessionId: Int, speakersResult: Result<List<SpeakerData>>, sessions: List<Session>) {
        val position = sessions.indexOfFirst { it.id == sessionId }
        if ((position > -1) and (position < sessions.size)) {
            val nextPageTitle = if (position == sessions.size - 1) "" else sessions[position + 1].title
            val session = sessions[position]
            val speakers: List<String> = (speakersResult as Result.Succeed<List<SpeakerData>>).data.toSpeakerStringList(session.speakers)
            this@SessionDetailViewModel._sessionDetail.postValue(session.toSessionDetail(speakers))
            if (sessions.first().id == sessions.last().id) {
                setFirstAndLast(true, true)
            } else if (session.id == sessions.first().id) {
                _nextSessionTitle.postValue(nextPageTitle)
                setFirstAndLast(true, false)
            } else if (session.id == sessions.last().id) {
                setFirstAndLast(false, true)
            } else {
                _nextSessionTitle.postValue(nextPageTitle)
                setFirstAndLast(false, false)
            }
        }
    }

    private fun List<SpeakerData>.toSpeakerStringList(speakerListOfSession: List<String>): List<String> {
        val list: List<Speaker> = intersect(speakerListOfSession, {
            speakerData: SpeakerData, s: String -> speakerData.id == s
        }).toSpeakerList()
        return list.map { speaker ->  speaker.fullName }
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

    private fun postErrorMessage(result: Result<Any>) {
        val errorMessage = result.getFailureMessage(context)
        _errorMessage.postValue(errorMessage)
    }
}
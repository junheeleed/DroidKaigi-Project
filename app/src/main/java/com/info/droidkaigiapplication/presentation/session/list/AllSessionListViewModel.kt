package com.info.droidkaigiapplication.presentation.session.list

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
import com.info.droidkaigiapplication.presentation.livedata.SessionSummariesLiveData
import com.info.droidkaigiapplication.presentation.session.list.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllSessionListViewModel(
        private val context: Context,
        private val roomRepository: RoomRepository,
        private val speakerRepository: SpeakerRepository,
        private val sessionRepository: SessionRepository)
    : ViewModel() {

    private val _isLoading: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _sessionSummaries: SessionSummariesLiveData = SessionSummariesLiveData()
    val sessionSummaries: LiveData<List<SessionSummary>> = _sessionSummaries

    fun loadAllSessionList() {
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

            if (isRoomOrSpeakerDataEmpty(roomsResult, speakersResult)) {
                withContext(Dispatchers.Main) {
                    _isLoading.postValue(false)
                }
                return@launch
            }

            val sessionSummaries = getSessionSummaries(roomsResult, speakersResult, sessionsResult)
            postSessionSummaries(sessionSummaries)
        }
    }

    private fun isRoomOrSpeakerDataEmpty(roomsResult: Result<List<RoomData>>,
                                         speakersResult: Result<List<SpeakerData>>): Boolean {
        return roomsResult.getData().isEmpty() or speakersResult.getData().isEmpty()
    }

    private fun getSessionSummaries(roomsResult: Result<List<RoomData>>,
                                    speakersResult: Result<List<SpeakerData>>,
                                    sessionsResult: Result<List<SessionData>>): List<SessionSummary> {
        val rooms = roomsResult.getData().toRoomList()
        val speakerSummaries = speakersResult.getData().toSpeakerSummaryList()
        return sessionsResult.getData().toSessionSummaryList(rooms, speakerSummaries)
    }

    private suspend fun postSessionSummaries(sessionSummaries: List<SessionSummary>) {
        withContext(Dispatchers.Main) {
            if (sessionSummaries.isNotEmpty()) {
                _errorMessage.postValue("")
            }
            this@AllSessionListViewModel._sessionSummaries.addAll(sessionSummaries)
            _isLoading.postValue(false)
        }
    }

    private suspend fun postErrorMessage(result: Result<List<Any>>) {
        val errorMessage = result.getFailureMessage(context)
        withContext(Dispatchers.Main) {
            _errorMessage.postValue(errorMessage)
            _isLoading.postValue(false)
        }
    }
}
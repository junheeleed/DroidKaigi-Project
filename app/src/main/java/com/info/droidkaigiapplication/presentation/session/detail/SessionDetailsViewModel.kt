package com.info.droidkaigiapplication.presentation.session.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.NotNullMutableLiveData
import com.info.droidkaigiapplication.presentation.getFailureMessage
import com.info.droidkaigiapplication.presentation.isFailed
import com.info.droidkaigiapplication.presentation.livedata.SessionsDetailLiveData
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail
import com.info.droidkaigiapplication.presentation.session.detail.model.toSessionDetailList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionDetailsViewModel(
        private val context: Context,
        private val sessionRepository: SessionRepository)
    : ViewModel() {

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _sessionDetails: SessionsDetailLiveData = SessionsDetailLiveData()
    val sessionDetails: LiveData<List<SessionDetail>> get() = _sessionDetails

    fun loadSessionList(sessionId: Int, roomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val sessionsResult = sessionRepository.getSessions()
            if (sessionsResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(sessionsResult)
                }
                return@launch
            }

            val sessionDetails = (sessionsResult as Result.Succeed<List<SessionData>>).data.toSessionDetailList().run {
                if (roomId > 0) {
                    this.filter { it.roomId == roomId }
                } else if ((sessionId > 0) and (roomId == 0)) {
                    this.filter { it.id == sessionId}
                } else {
                    this
                }
            }

            withContext(Dispatchers.Main) {
                _sessionDetails.addAll(sessionDetails)
            }
        }
    }

    fun getSessionDetailsSize() = _sessionDetails.size()

    private fun postErrorMessage(result: Result<List<SessionData>>) {
        val errorMessage = result.getFailureMessage(context)
        _errorMessage.postValue(errorMessage)
    }
}
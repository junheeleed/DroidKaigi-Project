package com.info.droidkaigiapplication.presentation.session.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.NetworkRequestFailureMessage
import com.info.droidkaigiapplication.presentation.NotNullMutableLiveData
import com.info.droidkaigiapplication.presentation.getFailureMessage
import com.info.droidkaigiapplication.presentation.isFailed
import com.info.droidkaigiapplication.presentation.livedata.SessionsLiveData
import com.info.droidkaigiapplication.presentation.session.model.Session
import com.info.droidkaigiapplication.presentation.session.model.toSessionList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllSessionListViewModel(
        private val context: Context,
        private val sessionRepository: SessionRepository)
    : ViewModel() {

    private val _isLoading: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _sessions: SessionsLiveData = SessionsLiveData()
    val sessions: LiveData<List<Session>> = _sessions

    fun loadAllSessionList() {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val sessionResult = sessionRepository.getSessions()
            if (sessionResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(sessionResult)
                    _isLoading.postValue(false)
                }
                return@launch
            }

            val sessions = ((sessionResult as Result.Succeed<List<SessionData>>).data).toSessionList()
            withContext(Dispatchers.Main) {
                if (sessions.isNotEmpty()) {
                    _errorMessage.postValue("")
                }
                this@AllSessionListViewModel._sessions.addAll(sessions)
                _isLoading.postValue(false)
            }
        }
    }

    private fun postErrorMessage(result: Result<List<SessionData>>) {
        val errorMessage = result.getFailureMessage(context)
        _errorMessage.postValue(errorMessage)
    }
}
package com.info.droidkaigiapplication.presentation.search

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
import com.info.droidkaigiapplication.presentation.isFailed
import com.info.droidkaigiapplication.presentation.livedata.SearchedSessionsLiveData
import com.info.droidkaigiapplication.presentation.search.model.SearchedSession
import com.info.droidkaigiapplication.presentation.search.model.toSearchedSessionList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
        private val context: Context,
        private val speakerRepository: SpeakerRepository,
        private val sessionRepository: SessionRepository)
    : ViewModel() {

    private val _isLoading: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _searchedSessions: SearchedSessionsLiveData = SearchedSessionsLiveData()
    val searchedSessions: LiveData<List<SearchedSession>> get() = _searchedSessions

    var keyword = ""

    fun search(keyword: String) {
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

            val sessionResult = sessionRepository.getSessions()
            if (sessionResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(sessionResult)
                    _isLoading.postValue(false)
                }
                return@launch
            }

            val speakerDataList = (speakersResult as Result.Succeed<List<SpeakerData>>).data
            val searchedSessions = ((sessionResult as Result.Succeed<List<SessionData>>).data).toSearchedSessionList(speakerDataList).filter {
                it.title.contains(keyword, true) or it.description.contains(keyword, true) or it.isContainedInSpeakers(keyword)
            }

            withContext(Dispatchers.Main) {
                if (searchedSessions.isNotEmpty()) {
                    _errorMessage.postValue("")
                }
                this@SearchViewModel.keyword = keyword
                this@SearchViewModel._searchedSessions.addAll(searchedSessions)
                _isLoading.postValue(false)
            }
        }
    }

    private fun SearchedSession.isContainedInSpeakers(keyword: String): Boolean {
        return this.speakers.any {
            it.contains(keyword, true)
        }
    }

    private fun postErrorMessage(result: Result<Any>) {
        val errorMessage = result.getFailureMessage(context)
        _errorMessage.postValue(errorMessage)
    }
}
package com.info.droidkaigiapplication.presentation.session

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.RoomRepository
import com.info.droidkaigiapplication.data.source.room.RoomData
import com.info.droidkaigiapplication.presentation.NotNullMutableLiveData
import com.info.droidkaigiapplication.presentation.getFailureMessage
import com.info.droidkaigiapplication.presentation.isFailed
import com.info.droidkaigiapplication.presentation.livedata.RoomsLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionsViewModel(
        private val context: Context,
        private val roomRepository: RoomRepository)
    : ViewModel() {

    private val _isLoading: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _rooms: RoomsLiveData = RoomsLiveData()
    val rooms: LiveData<List<Room>> get() = _rooms

    fun loadRooms() {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val roomsResult = roomRepository.getRooms()
            if (roomsResult.isFailed()) {
                withContext(Dispatchers.Main) {
                    postErrorMessage(roomsResult)
                    _isLoading.postValue(false)
                }
                return@launch
            }
            val rooms = ((roomsResult as Result.Succeed<List<RoomData>>).data).toRoomList()
            withContext(Dispatchers.Main) {
                if (rooms.isNotEmpty()) {
                    _errorMessage.postValue("")
                }
                val isSame = this@SessionsViewModel._rooms.getList() == rooms
                if (!isSame) {
                    this@SessionsViewModel._rooms.addAll(rooms)
                }
                _isLoading.postValue(false)
            }
        }
    }

    private fun postErrorMessage(roomResult: Result<List<RoomData>>) {
        val errorMessage = roomResult.getFailureMessage(context)
        _errorMessage.postValue(errorMessage)
    }
}
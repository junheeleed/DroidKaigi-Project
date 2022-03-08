package com.info.droidkaigiapplication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.info.droidkaigiapplication.MainCoroutineRule
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.RoomRepository
import com.info.droidkaigiapplication.data.source.room.RoomData
import com.info.droidkaigiapplication.mock
import com.info.droidkaigiapplication.presentation.session.SessionsViewModel
import com.info.droidkaigiapplication.presentation.session.list.model.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class SessionsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private var roomRepository = mock<RoomRepository>()

    private lateinit var sessionsViewModel: SessionsViewModel

    private val isLoadingObserver: Observer<Boolean> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val roomsObserver: Observer<List<Room>> = mock()

    private lateinit var isLoadingInOrder: InOrder

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        sessionsViewModel = SessionsViewModel(context, roomRepository)
        sessionsViewModel.isLoading.observeForever(isLoadingObserver)
        sessionsViewModel.errorMessage.observeForever(errorMessageObserver)
        sessionsViewModel.rooms.observeForever(roomsObserver)
        isLoadingInOrder = Mockito.inOrder(isLoadingObserver)
    }

    @Test
    fun room_Empty() {
        runBlockingTest {
            Mockito.`when`(roomRepository.getRooms()).thenReturn(Result.Succeed(listOf()))

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
        }
    }

    @Test
    fun room_NotEmpty() {
        runBlockingTest {
            val roomDataList: List<RoomData> = getRoomDataList(9)
            val result: Result.Succeed<List<RoomData>> = Result.Succeed(roomDataList)
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver, Mockito.times(2)).onChanged("")
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(roomsObserver).onChanged(capture() as List<Room>?)
                Assert.assertEquals(9, value.size)
            }
        }
    }

    @Test
    fun room_IsSame() {
        runBlockingTest {
            val roomDataList: List<RoomData> = getRoomDataList(9)
            val result: Result.Succeed<List<RoomData>> = Result.Succeed(roomDataList)

            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(roomsObserver).onChanged(capture() as List<Room>?)
                Assert.assertEquals(9, value.size)
            }


            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver, Mockito.times(3)).onChanged("")
        }
    }

    @Test
    fun room_IsNotSame() {
        runBlockingTest {
            val firstRoomDataList: List<RoomData> = getRoomDataList(9)
            val firstResult: Result.Succeed<List<RoomData>> = Result.Succeed(firstRoomDataList)

            Mockito.`when`(roomRepository.getRooms()).thenReturn(firstResult)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(roomsObserver, Mockito.times(1)).onChanged(capture() as List<Room>?)
                Assert.assertEquals(9, value.size)
            }

            val secondRoomDataList: List<RoomData> = getRoomDataList(3)
            val secondResult: Result.Succeed<List<RoomData>> = Result.Succeed(secondRoomDataList)

            Mockito.`when`(roomRepository.getRooms()).thenReturn(secondResult)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val secondCaptor = ArgumentCaptor.forClass(List::class.java)
            secondCaptor.run {
                Mockito.verify(roomsObserver, Mockito.times(2)).onChanged(capture() as List<Room>?)
                Assert.assertEquals(3, value.size)
            }

            Mockito.verify(errorMessageObserver, Mockito.times(3)).onChanged("")
        }
    }

    @Test
    fun room_Failure() {
        runBlockingTest {
            val result: Result.Failure = Result.Failure(-1, "failed")

            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun room_Error_SocketTimeOut() {
        runBlockingTest {
            val result: Result.Error = Result.Error(SocketTimeoutException())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun room_Error_UnknownHost() {
        runBlockingTest {
            val result: Result.Error = Result.Error(UnknownHostException())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun room_Error_Exception() {
        runBlockingTest {
            val result: Result.Error = Result.Error(Exception())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            sessionsViewModel.loadRooms()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    private fun getRoomDataList(size: Int): List<RoomData> {
        val roomDataList = arrayListOf<RoomData>()
        for (i in 1 .. size) {
            roomDataList.add(RoomData(i, i.toString(), i))
        }
        return roomDataList
    }
}
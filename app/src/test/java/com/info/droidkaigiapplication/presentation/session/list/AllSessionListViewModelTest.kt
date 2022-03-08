package com.info.droidkaigiapplication.presentation.session.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.info.droidkaigiapplication.MainCoroutineRule
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.RoomRepository
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.repository.SpeakerRepository
import com.info.droidkaigiapplication.data.source.room.RoomData
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.mock
import com.info.droidkaigiapplication.presentation.session.list.model.SessionSummary
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
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AllSessionListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private var roomRepository = mock<RoomRepository>()

    @Mock
    private var speakerRepository = mock<SpeakerRepository>()

    @Mock
    private var sessionRepository = mock<SessionRepository>()

    private lateinit var allSessionListViewModel: AllSessionListViewModel

    private val isLoadingObserver: Observer<Boolean> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val sessionSummariesObserver: Observer<List<SessionSummary>> = mock()

    private lateinit var isLoadingInOrder: InOrder

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        allSessionListViewModel = AllSessionListViewModel(context, roomRepository, speakerRepository, sessionRepository)
        allSessionListViewModel.isLoading.observeForever(isLoadingObserver)
        allSessionListViewModel.errorMessage.observeForever(errorMessageObserver)
        allSessionListViewModel.sessionSummaries.observeForever(sessionSummariesObserver)
        isLoadingInOrder = Mockito.inOrder(isLoadingObserver)
    }

    @Test
    fun session_Empty() {
        runBlockingTest {
            Mockito.`when`(roomRepository.getRooms()).thenReturn(Result.Succeed(listOf()))
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(Result.Succeed(listOf()))
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(Result.Succeed(listOf()))

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_NotEmpty() {
        runBlockingTest {
            Mockito.`when`(roomRepository.getRooms()).thenReturn(Result.Succeed(getRoomDataList()))
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(Result.Succeed(getSpeakerDataList()))
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(Result.Succeed(getSessionDataList()))

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver, Mockito.times(2)).onChanged("")
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(sessionSummariesObserver).onChanged(capture() as List<SessionSummary>?)
                Assert.assertEquals(10, value.size)
                val firstSessionSummary = value.first() as SessionSummary
                Assert.assertEquals("junheelee0", firstSessionSummary.speakerSummary.fullName)
                Assert.assertEquals("Room0", firstSessionSummary.room.name)

                val lastSessionSummary = value.last() as SessionSummary
                Assert.assertEquals("junheelee9", lastSessionSummary.speakerSummary.fullName)
                Assert.assertEquals("Room9", lastSessionSummary.room.name)
            }
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_Failure() {
        runBlockingTest {
            val result: Result.Failure = Result.Failure(-1, "failed")
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_Error_SocketTimeOut() {
        runBlockingTest {
            val result: Result.Error = Result.Error(SocketTimeoutException())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_Error_UnknownHost() {
        runBlockingTest {
            val result: Result.Error = Result.Error(UnknownHostException())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_Error_Exception() {
        runBlockingTest {
            val result: Result.Error = Result.Error(Exception())
            Mockito.`when`(roomRepository.getRooms()).thenReturn(result)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }


    private fun getRoomDataList(): List<RoomData> {
        val roomDataList: ArrayList<RoomData> = arrayListOf()
        for (i in 0 .. 9) {
            roomDataList.add(RoomData(i, "Room$i", i))
        }
        return roomDataList
    }

    private fun getSpeakerDataList(): List<SpeakerData> {
        val speakerDataList: ArrayList<SpeakerData> = arrayListOf()
        for (i in 0 .. 9) {
            speakerDataList.add(SpeakerData("speaker$i", "", "", "", "", "", false, listOf(), listOf(), "junheelee$i"))
        }
        return speakerDataList
    }

    private fun getSessionDataList(): List<SessionData> {
        val sessionDataList: ArrayList<SessionData> = arrayListOf()
        for (i in 0 .. 9) {
            sessionDataList.add(
                    SessionData(i, "title$i", "", "", "", false, false, listOf("speaker$i"), listOf(), listOf<Any>(), i, "", "")
            )
        }
        return sessionDataList
    }
}
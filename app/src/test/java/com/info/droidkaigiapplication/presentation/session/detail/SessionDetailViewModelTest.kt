package com.info.droidkaigiapplication.presentation.session.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.info.droidkaigiapplication.MainCoroutineRule
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.repository.SpeakerRepository
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.mock
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail
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
class SessionDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private val sessionRepository = mock<SessionRepository>()

    @Mock
    private val speakerRepository = mock<SpeakerRepository>()

    private lateinit var sessionDetailViewModel: SessionDetailViewModel

    private val isLoadingObserver: Observer<Boolean> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val sessionDetailObserver: Observer<SessionDetail> = mock()
    private val nexSessionTitleObserver: Observer<String> = mock()
    private val isFirstObserver: Observer<Boolean> = mock()
    private val isLastObserver: Observer<Boolean> = mock()
    private val isNavigateNeededObserver: Observer<Boolean> = mock()

    private lateinit var isLoadingInOrder: InOrder

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        sessionDetailViewModel = SessionDetailViewModel(context, sessionRepository, speakerRepository)
        sessionDetailViewModel.isLoading.observeForever(isLoadingObserver)
        sessionDetailViewModel.errorMessage.observeForever(errorMessageObserver)
        sessionDetailViewModel.sessionDetail.observeForever(sessionDetailObserver)
        sessionDetailViewModel.nextSessionTitle.observeForever(nexSessionTitleObserver)
        sessionDetailViewModel.isFirst.observeForever(isFirstObserver)
        sessionDetailViewModel.isLast.observeForever(isLastObserver)
        sessionDetailViewModel.isNavigateNeeded.observeForever(isNavigateNeededObserver)
        isLoadingInOrder = Mockito.inOrder(isLoadingObserver)
    }

    @Test
    fun sessionDetail_Empty() {
        runBlockingTest {
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(listOf())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)

            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(listOf())
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            sessionDetailViewModel.loadSession(0, 0)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(isNavigateNeededObserver, Mockito.times(2)).onChanged(false)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Single() {
        runBlockingTest {
            val size = 1
            val speakerDataList: List<SpeakerData> = getSpeakerDataList(size)
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(speakerDataList)
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)

            val sessionDataList: List<SessionData> = getSessionDataList(size)
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            sessionDetailViewModel.loadSession(1, 0)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(SessionDetail::class.java)
            captor.run {
                Mockito.verify(sessionDetailObserver, Mockito.times(2)).onChanged(capture())
                Assert.assertEquals("title1", value.title)
            }
            Mockito.verify(isFirstObserver).onChanged(true)
            Mockito.verify(isLastObserver).onChanged(true)
            Mockito.verify(isNavigateNeededObserver, Mockito.times(2)).onChanged(false)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Many_First() {
        runBlockingTest {
            val size = 10
            val speakerDataList: List<SpeakerData> = getSpeakerDataList(size)
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(speakerDataList)
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)

            val sessionDataList: List<SessionData> = getSessionDataList(size)
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            sessionDetailViewModel.loadSession(1, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(SessionDetail::class.java)
            captor.run {
                Mockito.verify(sessionDetailObserver, Mockito.times(2)).onChanged(capture())
                Assert.assertEquals("title1", value.title)
            }
            Mockito.verify(nexSessionTitleObserver).onChanged("title2")
            Mockito.verify(isFirstObserver).onChanged(true)
            Mockito.verify(isLastObserver, Mockito.times(2)).onChanged(false)
            Mockito.verify(isNavigateNeededObserver).onChanged(true)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Many_Last() {
        runBlockingTest {
            val speakerDataList: List<SpeakerData> = getSpeakerDataList(10)
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(speakerDataList)
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)

            val sessionDataList: List<SessionData> = getSessionDataList(10)
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            sessionDetailViewModel.loadSession(10, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(SessionDetail::class.java)
            captor.run {
                Mockito.verify(sessionDetailObserver, Mockito.times(2)).onChanged(capture())
                Assert.assertEquals("title10", value.title)
            }

            Mockito.verify(isFirstObserver, Mockito.times(2)).onChanged(false)
            Mockito.verify(isLastObserver).onChanged(true)
            Mockito.verify(isNavigateNeededObserver).onChanged(true)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Many_Middle() {
        runBlockingTest {
            val size = 10
            val speakerDataList: List<SpeakerData> = getSpeakerDataList(size)
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(speakerDataList)
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)

            val sessionDataList: List<SessionData> = getSessionDataList(size)
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            sessionDetailViewModel.loadSession(5, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(SessionDetail::class.java)
            captor.run {
                Mockito.verify(sessionDetailObserver, Mockito.times(2)).onChanged(capture())
                Assert.assertEquals("title5", value.title)
            }
            Mockito.verify(nexSessionTitleObserver).onChanged("title6")
            Mockito.verify(isFirstObserver, Mockito.times(2)).onChanged(false)
            Mockito.verify(isLastObserver, Mockito.times(2)).onChanged(false)
            Mockito.verify(isNavigateNeededObserver).onChanged(true)
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Failure() {
        runBlockingTest {
            val failureResult: Result.Failure = Result.Failure(-1, "error")
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(failureResult)

            sessionDetailViewModel.loadSession(1, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Error_SocketTimeOut() {
        runBlockingTest {
            val result = Result.Error(SocketTimeoutException())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            sessionDetailViewModel.loadSession(1, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Error_UnknownHost() {
        runBlockingTest {
            val result = Result.Error(UnknownHostException())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            sessionDetailViewModel.loadSession(1, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun sessionDetail_Error_Exception() {
        runBlockingTest {
            val result = Result.Error(Exception())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            sessionDetailViewModel.loadSession(1, 1)

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    private fun getSpeakerDataList(size: Int): List<SpeakerData> {
        val speakerList: ArrayList<SpeakerData> = arrayListOf()
        for (i in 1 .. size) {
            speakerList.add(
                    SpeakerData(
                            i.toString(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            false,
                            listOf(),
                            listOf(),
                            ""
                    )
            )
        }
        return speakerList
    }

    private fun getSessionDataList(size: Int): List<SessionData> {
        val sessionDataList: ArrayList<SessionData> = arrayListOf()
        for (i in 1 .. size) {
            sessionDataList.add(
                    SessionData(
                            i,
                            "title$i",
                            "",
                            "",
                            "",
                            false,
                            false,
                            listOf(i.toString()),
                            listOf(),
                            listOf<Any>(),
                            1,
                            "",
                            ""
                    )
            )
        }
        return sessionDataList
    }
}
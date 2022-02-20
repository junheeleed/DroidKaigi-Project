package com.info.droidkaigiapplication.presentation.search

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
import com.info.droidkaigiapplication.presentation.search.model.SearchedSession
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
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private val sessionRepository = mock<SessionRepository>()

    @Mock
    private val speakerRepository = mock<SpeakerRepository>()

    private lateinit var searchViewModel: SearchViewModel

    private val isLoadingObserver: Observer<Boolean> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val searchedSessionsObserver: Observer<List<SearchedSession>> = mock()

    private lateinit var isLoadingInOrder: InOrder

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        searchViewModel = SearchViewModel(context, speakerRepository, sessionRepository)
        searchViewModel.isLoading.observeForever(isLoadingObserver)
        searchViewModel.errorMessage.observeForever(errorMessageObserver)
        searchViewModel.searchedSessions.observeForever(searchedSessionsObserver)
        isLoadingInOrder = Mockito.inOrder(isLoadingObserver)
    }

    @Test
    fun searchedSession_Empty() {
        runBlockingTest {
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(listOf())
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(listOf())

            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Assert.assertEquals("android", searchViewModel.keyword)
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(searchedSessionsObserver).onChanged(capture() as List<SearchedSession>?)
                Assert.assertEquals(0, value.size)
            }
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun searchedSession_NotEmpty() {
        runBlockingTest {
            val speakerDataList: List<SpeakerData> = getSpeakerDataList()
            val speakerDataListResult: Result.Succeed<List<SpeakerData>> = Result.Succeed(speakerDataList)
            val sessionDataList: List<SessionData> = getSessionDataList()
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)

            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(speakerDataListResult)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Assert.assertEquals("android", searchViewModel.keyword)
            Mockito.verify(errorMessageObserver, Mockito.times(2)).onChanged("")
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(searchedSessionsObserver).onChanged(capture() as List<SearchedSession>?)
                Assert.assertEquals(7, value.size)
                Assert.assertEquals("Android Tester Lee", ((value.first() as SearchedSession).speakers.first()))
            }
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun searchedSession_Failure() {
        runBlockingTest {
            val result: Result.Failure = Result.Failure(-1, "error")

            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun searchedSession_Error_SocketTimeOut() {
        runBlockingTest {
            val result = Result.Error(SocketTimeoutException())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun searchedSession_Error_UnKnownHost() {
        runBlockingTest {
            val result = Result.Error(UnknownHostException())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Cellular or Wifi is not turn on.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun searchedSession_Error_Exception() {
        runBlockingTest {
            val result = Result.Error(Exception())
            Mockito.`when`(speakerRepository.getSpeakers()).thenReturn(result)

            searchViewModel.search("android")

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    private fun getSpeakerDataList(): List<SpeakerData> {
        val speakerDataList: ArrayList<SpeakerData> = arrayListOf()
        speakerDataList.add(SpeakerData("AAA", "Android Tester", "Lee", "", "", "", false, listOf(), listOf(), "Android Tester Lee"))
        speakerDataList.add(SpeakerData("BBB", "Android Tester", "Kim", "", "", "", false, listOf(), listOf(), "Android Tester Kim"))
        return speakerDataList
    }

    private fun getSessionDataList(): List<SessionData> {
        val sessionDataList: ArrayList<SessionData> = arrayListOf()
        for (i in 1 .. 10) {
            if (i % 2 == 0) {
                val session = SessionData(0,
                        "android",
                        "desciption",
                        "",
                        "",
                        false,
                        false,
                        listOf("AAA"),
                        listOf(),
                        listOf<Any>(),
                        0,
                        "",
                        ""
                )
                sessionDataList.add(session)
            } else if (i % 3 == 0) {
                val session = SessionData(0,
                        "",
                        "desciption",
                        "",
                        "",
                        false,
                        false,
                        listOf("BBB"),
                        listOf(),
                        listOf<Any>(),
                        0,
                        "",
                        ""
                )
                sessionDataList.add(session)
            }
        }
        return sessionDataList
    }
}
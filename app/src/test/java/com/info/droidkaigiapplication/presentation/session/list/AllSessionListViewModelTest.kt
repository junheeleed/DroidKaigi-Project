package com.info.droidkaigiapplication.presentation.session.list

import com.info.droidkaigiapplication.presentation.session.model.Session
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.info.droidkaigiapplication.MainCoroutineRule
import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.repository.SessionRepository
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.mock
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
    private var sessionRepository = mock<SessionRepository>()

    private lateinit var allSessionListViewModel: AllSessionListViewModel

    private val isLoadingObserver: Observer<Boolean> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val sessionsObserver: Observer<List<Session>> = mock()

    private lateinit var isLoadingInOrder: InOrder

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        allSessionListViewModel = AllSessionListViewModel(context, sessionRepository)
        allSessionListViewModel.isLoading.observeForever(isLoadingObserver)
        allSessionListViewModel.errorMessage.observeForever(errorMessageObserver)
        allSessionListViewModel.sessions.observeForever(sessionsObserver)
        isLoadingInOrder = Mockito.inOrder(isLoadingObserver)
    }

    @Test
    fun session_Empty() {
        runBlockingTest {
            val sessionDataListResult: Result.Succeed<List<SessionData>> = Result.Succeed(listOf())
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionDataListResult)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(sessionsObserver).onChanged(capture() as List<Session>?)
                Assert.assertEquals(0, value.size)
            }
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_NotEmpty() {
        runBlockingTest {
            val sessionDataList: List<SessionData> = getSessionDataList(10)
            val sessionListDataResult: Result.Succeed<List<SessionData>> = Result.Succeed(sessionDataList)
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(sessionListDataResult)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver, Mockito.times(2)).onChanged("")
            val captor = ArgumentCaptor.forClass(List::class.java)
            captor.run {
                Mockito.verify(sessionsObserver).onChanged(capture() as List<Session>?)
                Assert.assertEquals(10, value.size)
            }
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    @Test
    fun session_Failure() {
        runBlockingTest {
            val result: Result.Failure = Result.Failure(-1, "failed")
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(result)

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
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(result)

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
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(result)

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
            Mockito.`when`(sessionRepository.getSessions()).thenReturn(result)

            allSessionListViewModel.loadAllSessionList()

            isLoadingInOrder.verify(isLoadingObserver).onChanged(true)
            Mockito.verify(errorMessageObserver).onChanged("Failed to load data.")
            isLoadingInOrder.verify(isLoadingObserver).onChanged(false)
        }
    }

    private fun getSessionDataList(size: Int): List<SessionData> {
        val sessionDataList: ArrayList<SessionData> = arrayListOf()
        for (i in 1 .. size) {
            sessionDataList.add(
                    SessionData(i, "title$i", "", "", "", false, false, listOf(), listOf(), listOf<Any>(), 1, "", "")
            )
        }
        return sessionDataList
    }
}
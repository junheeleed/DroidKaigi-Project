package com.info.droidkaigiapplication.di

import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.data.repository.*
import com.info.droidkaigiapplication.data.source.room.RoomDataSource
import com.info.droidkaigiapplication.data.source.room.RoomWebDataSource
import com.info.droidkaigiapplication.data.source.sessions.SessionDataSource
import com.info.droidkaigiapplication.data.source.sessions.SessionWebDataSource
import com.info.droidkaigiapplication.data.source.speakers.SpeakerDataSource
import com.info.droidkaigiapplication.data.source.speakers.SpeakerWebDataSource
import com.info.droidkaigiapplication.presentation.search.SearchViewModel
import com.info.droidkaigiapplication.presentation.session.list.SessionListViewModel
import com.info.droidkaigiapplication.presentation.session.SessionsViewModel
import com.info.droidkaigiapplication.presentation.session.detail.SessionDetailViewModel
import com.info.droidkaigiapplication.presentation.session.detail.list.SessionDetailsViewModel
import com.info.droidkaigiapplication.presentation.session.list.AllSessionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sessionRecyclerViewPool = module {
    single { RecyclerView.RecycledViewPool() }
}

val viewModelModule = module {
    viewModel { SessionsViewModel(get(), get()) }
    viewModel { AllSessionListViewModel(get(), get(), get(), get()) }
    viewModel { SessionListViewModel(get(), get(), get(), get()) }
    viewModel { SessionDetailsViewModel(get(), get()) }
    viewModel { SessionDetailViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
}

val repositoryModule = module {
    factory<SessionRepository> { SessionRepositoryImpl(get()) }
    factory<SpeakerRepository> { SpeakerRepositoryImpl(get()) }
    factory<RoomRepository> { RoomRepositoryImpl(get()) }
}

val dataSourceModule = module {
    single<SessionDataSource> { SessionWebDataSource() }
    single<SpeakerDataSource> { SpeakerWebDataSource() }
    single<RoomDataSource> { RoomWebDataSource() }
}
package com.example.movieapp.di

import com.example.movieapp.services.repository.MovieRepository
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import com.example.movieapp.services.repository.remoteApi.TmdbService
import com.example.movieapp.services.repository.remoteApi.client
import com.example.movieapp.services.repository.remoteApi.jsonFormatter
import com.example.movieapp.viewModel.MainViewModel
import com.example.movieapp.viewModel.ReminderListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ReminderListViewModel(get()) }
}

val appModule = module {

    single<TmdbService> { object : TmdbService(key, jsonFormatter, get()) {} }

    factory { client }

    single { get<MovieRoomDatabase>().movieDao() }

    factory { MovieRepository(get(), get()) }

}

const val key = "5e2a1cba50d2f209f83dbd7aef1d615e"
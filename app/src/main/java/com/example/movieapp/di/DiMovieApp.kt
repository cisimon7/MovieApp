package com.example.movieapp.di

import android.app.Application
import com.example.movieapp.services.repository.localDb.MovieRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DiMovieApp : Application() {

    private val database by lazy { MovieRoomDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DiMovieApp)
            modules(module { single { database } }, appModule, viewModelModule)
        }
    }
}
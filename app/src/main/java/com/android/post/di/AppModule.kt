package com.android.post.di

import com.android.post.data.Repository
import com.android.post.data.local.AppDatabase
import com.android.post.ui.detail.DetailViewModel
import com.android.post.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { AppDatabase.getDatabase(androidContext()).articleDao() }
    // Assuming you have a Repository class that takes a data source as a parameter
    single { Repository(get(), get()) }
    // viewModel org.koin.androidx.viewmodel.dsl.viewModel
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
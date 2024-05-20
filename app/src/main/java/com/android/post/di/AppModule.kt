package com.android.post.di

import androidx.room.Room
import com.android.post.data.Repository
import com.android.post.data.local.AppDatabase
import com.android.post.data.remote.RemoteDataSource
import com.android.post.presentation.posts.ArticleViewModel
import com.android.post.presentation.posts.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_db").build() }
    single { get<AppDatabase>().articleDao() }
    single { RemoteDataSource(get()) }
    single { Repository(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
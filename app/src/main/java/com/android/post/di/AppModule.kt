package com.android.post.di

import androidx.room.Room
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app-db").build() }
    single { get<AppDatabase>().articleDao() }
    single { ArticleRepository(get(), get()) }
    viewModel { MainViewModel(get()) }
}

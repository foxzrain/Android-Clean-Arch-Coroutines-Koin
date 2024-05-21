package com.android.post.di

import androidx.room.Room
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.data.remote.ApiService
import com.android.post.ui.main.MainActivity
import com.android.post.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Create Retrofit instance
val apiService: ApiService = Retrofit.Builder()
    .baseUrl("https://medium.com/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(ApiService::class.java)

// Create Room database
val appDatabase: AppDatabase =
    Room.databaseBuilder(MainActivity().applicationContext, AppDatabase::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

val AppModule = module {
    single { apiService }
    single { appDatabase }
    single { get<AppDatabase>().articleDao() }
    single { ArticleRepository(get(), get()) }
    viewModel { MainViewModel(get()) }
}



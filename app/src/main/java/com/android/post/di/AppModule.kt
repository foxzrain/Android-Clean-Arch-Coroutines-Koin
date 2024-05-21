package com.android.post.di

import androidx.room.Room
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.data.remote.ApiService
import com.android.post.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val AppModule = module {
    val appModule = module {
        single {
            Retrofit.Builder()
                .baseUrl("https://medium.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        single {
            Room.databaseBuilder(get(), AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        single { get<AppDatabase>().articleDao() }

        single { ArticleRepository(get<AppDatabase>().articleDao(), get<ApiService>()) }

        viewModel { MainViewModel(get<ArticleRepository>()) }
    }

}



package com.android.post.di

import androidx.room.Room
import com.android.post.data.database.AppDatabase
import com.android.post.presentation.posts.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ApiModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "article-database").build() }
    single { get<AppDatabase>().articleDao() }
    viewModel { ArticleViewModel(get()) }
}
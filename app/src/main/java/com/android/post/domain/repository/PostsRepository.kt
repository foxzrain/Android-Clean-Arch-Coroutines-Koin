package com.android.post.domain.repository

import com.android.post.domain.model.XmlFeed

interface PostsRepository {

    suspend fun getPosts(): List<XmlFeed>
}
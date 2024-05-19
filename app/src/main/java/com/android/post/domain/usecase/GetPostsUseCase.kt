package com.android.post.domain.usecase

import com.android.post.domain.model.XmlFeed
import com.android.post.domain.repository.PostsRepository
import com.android.post.domain.usecase.base.UseCase

class GetPostsUseCase(
    private val postsRepository: PostsRepository
) : UseCase<List<XmlFeed>, Any?>() {

    override suspend fun run(params: Any?): List<XmlFeed> {
        return postsRepository.getPosts()
    }

}
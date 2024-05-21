package com.android.post.domain.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.post.data.local.ArticleEntity
import com.example.post.databinding.ItemArticalBinding

class ArticleAdapter(private val onClick: (ArticleEntity) -> Unit) :
    ListAdapter<ArticleEntity, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class ArticleViewHolder(private val binding: ItemArticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity, onClick: (ArticleEntity) -> Unit) {
            binding.article = article
            binding.root.setOnClickListener { onClick(article) }
            binding.executePendingBindings()
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ArticleEntity>() {
        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem == newItem
        }
    }
}
package com.android.post.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.post.data.model.ArticleEntity
import com.android.post.databinding.ItemArticleBinding

class MainAdapter(
    private var articles: List<ArticleEntity>,
    private val onClick: (ArticleEntity) -> Unit
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity) {
            binding.article = article
            binding.root.setOnClickListener {
                onClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    fun updateData(newArticles: List<ArticleEntity>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<ArticleEntity>?) {
        items?.let {
            (recyclerView.adapter as MainAdapter).updateData(it)
        }
    }
}



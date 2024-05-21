package com.android.post.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.post.domain.model.ArticleAdapter
import com.android.post.ui.detail.DetailActivity
import com.example.post.R
import com.example.post.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ContentLoadingProgressBar
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        progressBar = binding.loading

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("ARTICLE_ID", article.id)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = articleAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                articleAdapter.submitList(it)
                progressBar.hide()
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.show()
            } else {
                progressBar.hide()
            }
        })
    }
}
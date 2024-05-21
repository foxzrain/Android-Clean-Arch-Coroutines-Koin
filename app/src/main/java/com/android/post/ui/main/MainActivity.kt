package com.android.post.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.android.post.R
import com.android.post.databinding.ActivityMainBinding
import com.android.post.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MainAdapter(emptyList()) { article ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("article", article)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        viewModel.articles.observe(this, Observer { articles ->
            adapter.updateData(articles)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        })

        viewModel.refreshArticles()
    }
}


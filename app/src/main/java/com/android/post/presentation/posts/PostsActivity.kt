package com.android.post.presentation.posts

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.post.utils.isNetworkAvailable
import com.example.post.R
import com.example.post.databinding.ActivityPostsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostsActivity : AppCompatActivity() {

    private lateinit var activityPostsBinding: ActivityPostsBinding
    private var mAdapter: PostsAdapter? = PostsAdapter()
    private val postViewModel: PostsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPostsBinding = DataBindingUtil.setContentView(this, R.layout.activity_posts)

        activityPostsBinding.postsRecyclerView.adapter = mAdapter

        if (isNetworkAvailable()) {
            postViewModel.getPosts()
        } else {
            Toast.makeText(
                this,
                getString(R.string.no_internet_connection),
                LENGTH_SHORT
            ).show()
        }

        with(postViewModel) {

            postsData.observe(this@PostsActivity) {
                activityPostsBinding.postsProgressBar.visibility = GONE
                mAdapter?.mPostList = it
            }

            messageData.observe(this@PostsActivity) {
                Toast.makeText(this@PostsActivity, it, LENGTH_LONG).show()
            }

            showProgressbar.observe(this@PostsActivity) { isVisible ->
                activityPostsBinding.postsProgressBar.visibility = if (isVisible) VISIBLE else GONE
            }
        }
    }


    override fun onDestroy() {
        mAdapter = null
        super.onDestroy()
    }

    companion object {
        private val TAG = PostsActivity::class.java.name
    }
}

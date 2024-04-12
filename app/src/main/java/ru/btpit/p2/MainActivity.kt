package ru.btpit.p2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dto.Post
import ru.btpit.p2.databinding.ActivityPostsBinding
import ru.netology.nmedia.util.AndroidUtils
import viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsActivity (object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.share(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.save.setOnClickListener {
            with(binding.content)
            {
                if(text.isNullOrBlank()){
                    Toast.makeText(
                        this@MainActivity,
                        "Введите текст",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        viewModel.edited.observe(this) {post ->
        if (post.id == 0L)
        {
            return@observe
        }
            with(binding.content)
            {
                requestFocus()
                setText(post.content)
            }
        }
    }
}
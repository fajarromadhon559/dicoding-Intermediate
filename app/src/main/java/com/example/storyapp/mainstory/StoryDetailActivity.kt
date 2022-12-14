package com.example.storyapp.mainstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.local_data.Entity
import com.example.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDetail()
    }

    private fun getDetail() {
        val image = binding.ivImage
        val name = binding.tvName
        val description = binding.tvDescription
        val detail = intent.getParcelableExtra<Entity>("story") as Entity
        Glide.with(this)
            .load(detail.photoUrl)
            .into(image)
        binding.tvName.text = detail.name
        description.text = detail.description
    }
}
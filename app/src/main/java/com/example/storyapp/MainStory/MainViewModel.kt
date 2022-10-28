package com.example.storyapp.MainStory

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository

class MainViewModel (private val repository: Repository) : ViewModel() {

    fun getListStory(apiKey: String) = repository.getListStory(apiKey)

}
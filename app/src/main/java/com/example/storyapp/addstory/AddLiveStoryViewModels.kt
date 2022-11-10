package com.example.storyapp.addstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository
import java.io.File

class AddLiveStoryViewModels (private val repository: Repository): ViewModel() {
    fun uploadStory(token:String ,description: String,file: File) = repository.uploadLiveStory(token,description,file)

}
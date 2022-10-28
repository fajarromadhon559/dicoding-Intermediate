package com.example.storyapp.AddStory

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddLiveStoryViewModels (private val repository: Repository): ViewModel() {
    fun uploadStory(token:String, description: RequestBody, file: MultipartBody.Part) = repository.upLoadStory(token ,description,file)

}
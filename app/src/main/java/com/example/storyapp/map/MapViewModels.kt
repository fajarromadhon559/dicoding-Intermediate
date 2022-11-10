package com.example.storyapp.map

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository

class MapViewModels(private val repository: Repository): ViewModel() {
    fun getStoryByMaps(location :Int, token:String) = repository.getListStoryByMaps(location,token)
}
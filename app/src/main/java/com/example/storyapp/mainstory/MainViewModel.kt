package com.example.storyapp.mainstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.Repo.Repository
import com.example.storyapp.local_data.Entity

class MainViewModel (private val repository: Repository) : ViewModel() {

    val storyList: LiveData<PagingData<Entity>> =
        repository.getStoryList().cachedIn(viewModelScope)

}
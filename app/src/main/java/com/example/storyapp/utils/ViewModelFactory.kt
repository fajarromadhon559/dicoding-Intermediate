package com.example.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.addstory.AddLiveStoryViewModels
import com.example.storyapp.login.LoginViewModels
import com.example.storyapp.mainstory.MainViewModel
import com.example.storyapp.register.RegisterViewModels
import com.example.storyapp.Repo.Injection
import com.example.storyapp.Repo.Repository
import com.example.storyapp.map.MapViewModels

class ViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModels::class.java) -> {
                LoginViewModels(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModels::class.java) -> {
                RegisterViewModels(repository) as T
            }
            modelClass.isAssignableFrom(AddLiveStoryViewModels::class.java) -> {
                AddLiveStoryViewModels(repository) as T
            }
            modelClass.isAssignableFrom(MapViewModels::class.java) -> {
                MapViewModels(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
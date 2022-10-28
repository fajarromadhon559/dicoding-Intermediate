package com.example.storyapp.Repo

import android.content.Context
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.Local.LiveDB

object Injection {
    fun provideRepository(context : Context) : Repository{
        val apiService = ApiConfig.getApiService()
        val database = LiveDB.getInstance(context)
        val dao = database.storyDao()
        return Repository.getInstance(apiService,dao)
    }
}
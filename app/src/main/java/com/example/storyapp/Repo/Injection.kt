package com.example.storyapp.Repo

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.local_data.LiveDB

object Injection {
    fun provideRepository(context : Context) : Repository{
        val apiService = ApiConfig.getApiService()
        val database = LiveDB.getInstance(context)
        return Repository.getInstance(apiService,database)
    }
}
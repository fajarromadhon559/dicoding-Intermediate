package com.example.storyapp.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.storyapp.API.ApiService
import com.example.storyapp.Local.Entity
import com.example.storyapp.Local.MemberDao
import com.example.storyapp.Response.LoginResult
import com.example.storyapp.Response.RegisterResponse
import com.example.storyapp.Response.UploadDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val apiService: ApiService,
    private val memberDao: MemberDao
) {
    fun register(
        name : String,
        email : String,
        password : String,): LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Loading)
        try {
            val response = apiService.userRegister(
                name,
                email,
                password,
            )
            if (response.error){
                emit(Result.Error(response.massage))
            }else {
                emit(Result.Success(response))
            }
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email : String,
        password : String
    ): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.userLogin(
                email,
                password
            )
            if (response.error){
                Log.d("error response", "true: ${response.error} ")
                emit(Result.Error(response.message))
            }else {
                Log.d("error response", "false: ${response.error} ")
                emit(Result.Success(response))
            }
    } catch (e : Exception){
            Log.d("StoryRepository", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun upLoadStory(
        token : String,
        description : RequestBody,
        file : MultipartBody.Part
    ): LiveData<Result<UploadDataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(
                "Bearer $token",
                description,
                file
            )
            if (response.error){
                emit(Result.Error(response.message))
            }
            else{
                emit(Result.Success(response))
            }
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListStory(apiKey : String)
    : LiveData<Result<List<Entity>>> = liveData {
        try {
            val response = apiService.getAllListStory("Bearer $apiKey")
            val stories = response.storyResponseItems
            val listStory = stories.map { story ->
                Entity(
                    story.id,
                    story.name,
                    story.description,
                    story.photoUrl,
                    story.createdAt
                )
            }
            memberDao.insertStory(listStory)
        } catch (e : Exception){
            Log.d("Repository", "getListStory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData : LiveData<Result<List<Entity>>>
        = memberDao.getStory().map { Result.Success(it) }
        emitSource(localData)
    }

    companion object{
        @Volatile
        private var instance : Repository? = null
        fun getInstance(
            apiService: ApiService,
            memberDao: MemberDao
        ): Repository = instance?: synchronized(this){
            instance?: Repository(apiService,memberDao)
        }.also { instance = it }
    }

}
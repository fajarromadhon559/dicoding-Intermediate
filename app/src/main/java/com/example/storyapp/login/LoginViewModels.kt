package com.example.storyapp.login

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository

class LoginViewModels(private val repository: Repository) : ViewModel() {
    fun loginUser(email: String, password: String) = repository.login(email,password)
}
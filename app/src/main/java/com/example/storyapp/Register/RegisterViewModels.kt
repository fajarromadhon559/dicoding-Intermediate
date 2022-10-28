package com.example.storyapp.Register

import androidx.lifecycle.ViewModel
import com.example.storyapp.Repo.Repository

class RegisterViewModels(private val repository: Repository) : ViewModel() {
    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) = repository.register(name , email, password)
}
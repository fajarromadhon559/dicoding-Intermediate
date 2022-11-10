package com.example.storyapp.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.Repo.Repository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.Repo.Result
import com.example.storyapp.response.LoginResult
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelsTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModels
    private var dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModels(repository)
    }

    @Test
    fun `when login success Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<Result<LoginResult>>()
        expectedResponse.value = Result.Success(dummyLoginResponse)
        Mockito.`when`(repository.login("fajar@yandex.com", "qwerty"))
            .thenReturn(expectedResponse)

        val actualLoginResponse = loginViewModel.loginUser("fajar@yandex.com", "qwerty").getOrAwaitValue()
        Mockito.verify(repository).login("fajar@yandex.com", "qwerty")
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success)
        assertEquals(dummyLoginResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `when login failed or error should return error`() {
        val loginResponse = MutableLiveData<Result<LoginResult>>()
        loginResponse.value = Result.Error("Error")
        Mockito.`when`(repository.login("fajar@yandex.com", "qwerty")).thenReturn(loginResponse)

        val actualResponse = loginViewModel.loginUser("fajar@yandex.com", "qwerty").getOrAwaitValue()
        Mockito.verify(repository).login("fajar@yandex.com", "qwerty")
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}
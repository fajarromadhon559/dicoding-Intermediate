package com.example.storyapp.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.Repo.Repository
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.Repo.Result
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
class RegisterViewModelsTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var registerViewModel: RegisterViewModels
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterReponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModels(repository)
    }

    @Test
    fun `when register success Should Not Null and Return Success`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(repository.register("fajar", "fajar@yandex.com", "qwerty"))
            .thenReturn(expectedResponse)

        val actualLoginResponse = registerViewModel.registerUser("fajar", "fajar@yandex.com", "qwerty").getOrAwaitValue()
        Mockito.verify(repository).register("fajar", "fajar@yandex.com", "qwerty")
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success)
        assertEquals(dummyRegisterResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `when register failed or error should return error`() {
        val registerResponse = MutableLiveData<Result<RegisterResponse>>()
        registerResponse.value = Result.Error("Error")
        Mockito.`when`(repository.register("fajar", "fajar@yandex.com", "qwerty")).thenReturn(registerResponse)

        val actualResponse = registerViewModel.registerUser("fajar", "fajar@yandex.com", "qwerty").getOrAwaitValue()
        Mockito.verify(repository).register("fajar", "fajar@yandex.com", "qwerty")
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}
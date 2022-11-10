package com.example.storyapp.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.Repo.Repository
import com.example.storyapp.response.UploadDataResponse
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
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddLiveStoryViewModelsTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var addStoryViewModel: AddLiveStoryViewModels
    private var dummyAddStoryResponse = DataDummy.generateUploadDataResponse()

    @Mock
    private lateinit var dummyMockFile : File

    @Before
    fun setUp() {
        addStoryViewModel = AddLiveStoryViewModels(repository)
    }

    @Test
    fun `when add story success should not null and return success`() {
        val expectedResponse = MutableLiveData<Result<UploadDataResponse>>()
        expectedResponse.value = Result.Success(dummyAddStoryResponse)
        Mockito.`when`(repository.uploadLiveStory("bearer", "foto", dummyMockFile)).thenReturn(expectedResponse)

        val actualUploadResponse = addStoryViewModel.uploadStory("bearer", "foto", dummyMockFile).getOrAwaitValue()
        Mockito.verify(repository).uploadLiveStory("bearer", "foto", dummyMockFile)
        assertNotNull(actualUploadResponse)
        assertTrue(actualUploadResponse is Result.Success)
        assertEquals(dummyAddStoryResponse, (actualUploadResponse as Result.Success).data)
    }

    @Test
    fun `when add Story failed or error should return error`() {
        val uploadResponse = MutableLiveData<Result<UploadDataResponse>>()
        uploadResponse.value = Result.Error("Error")
        Mockito.`when`(repository.uploadLiveStory("bearer", "foto", dummyMockFile)).thenReturn(uploadResponse)

        val actualResponse = addStoryViewModel.uploadStory("bearer", "foto", dummyMockFile).getOrAwaitValue()
        Mockito.verify(repository).uploadLiveStory("bearer", "foto", dummyMockFile)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}
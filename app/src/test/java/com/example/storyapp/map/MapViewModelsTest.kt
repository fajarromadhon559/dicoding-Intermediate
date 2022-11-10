package com.example.storyapp.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.Repo.Repository
import com.example.storyapp.response.StoryResponseItem
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import com.example.storyapp.Repo.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelsTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var mapsViewModel: MapViewModels
    private val dummyMarker = DataDummy.generateDummyUserLocation()

    @Before
    fun setUp() {
        mapsViewModel = MapViewModels(repository)
    }

    @Test
    fun `when Get Mark List Should Not Null and Return Success`() {
        val expectedMarker = MutableLiveData<Result<List<StoryResponseItem>>>()
        expectedMarker.value = Result.Success(dummyMarker)
        Mockito.`when`(repository.getListStoryByMaps(1, "Bearer")).thenReturn(expectedMarker)

        val actualMarker = mapsViewModel.getStoryByMaps(1, "Bearer").getOrAwaitValue()
        Mockito.verify(repository).getListStoryByMaps(1, "Bearer")
        assertNotNull(actualMarker)
        assertTrue(actualMarker is Result.Success)
        assertEquals(dummyMarker.size, (actualMarker as Result.Success).data.size)
    }

    @Test
    fun `when network error should return error`() {
        val markList = MutableLiveData<Result<List<StoryResponseItem>>>()
        markList.value = Result.Error("Error")
        Mockito.`when`(repository.getListStoryByMaps(1, "Bearer")).thenReturn(markList)

        val actualMark = mapsViewModel.getStoryByMaps(1, "Bearer").getOrAwaitValue()
        Mockito.verify(repository).getListStoryByMaps(1, "Bearer")
        assertNotNull(actualMark)
        assertTrue(actualMark is Result.Error)
    }
}
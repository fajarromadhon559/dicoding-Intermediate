package com.example.storyapp.Repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.storyapp.addstory.AddLiveStoryViewModels
import com.example.storyapp.local_data.Entity
import com.example.storyapp.login.LoginViewModels
import com.example.storyapp.mainstory.StoryListAdapter
import com.example.storyapp.map.MapViewModels
import com.example.storyapp.register.RegisterViewModels
import com.example.storyapp.response.LoginResult
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.response.StoryResponseItem
import com.example.storyapp.response.UploadDataResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRules
import com.example.storyapp.utils.PagedDataSource
import com.example.storyapp.utils.PagedDataSource.Companion.listUpdateCallback
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import kotlin.Result.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRules()

    @Mock
    private lateinit var registerViewModel: RegisterViewModels
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterReponse()
    private lateinit var loginViewModel: LoginViewModels
    private var dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private lateinit var addStoryViewModel: AddLiveStoryViewModels
    private var dummyAddStoryResponse = DataDummy.generateUploadDataResponse()
    private lateinit var mapsViewModel: MapViewModels
    private val dummyMarker = DataDummy.generateDummyUserLocation()
    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var dummyMockFile: File


    @Before
    fun setUp() {
        registerViewModel = RegisterViewModels(repository)
        loginViewModel = LoginViewModels(repository)
        addStoryViewModel = AddLiveStoryViewModels(repository)
        mapsViewModel = MapViewModels(repository)
    }


    @Test
    fun `when register success must not null and success`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(repository.register("fajar", "fajar@yandex.com", "qwerty"))
            .thenReturn(expectedResponse)
        val actualLoginResponse =
            registerViewModel.registerUser("fajar", "fajar@yandex.com", "qwerty")
                .getOrAwaitValue()
        Mockito.verify(repository).register("fajar", "fajar@yandex.com", "qwerty")
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success)
        assertEquals(dummyRegisterResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `when login success must not null and success`() {

        val expectedResponse = MutableLiveData<Result<LoginResult>>()
        expectedResponse.value = Result.Success(dummyLoginResponse)
        Mockito.`when`(repository.login("fajar@yandex.com", "qwerty"))
            .thenReturn(expectedResponse)
        val actualLoginResponse =
            loginViewModel.loginUser("fajar@yandex.com", "qwerty").getOrAwaitValue()
        Mockito.verify(repository).login("fajar@yandex.com", "qwerty")
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success<*>)
        assertEquals(dummyLoginResponse, (actualLoginResponse as Result.Success).data)

    }

    @Test
    fun `when upload story success must not null and success`() {

        val expectedResponse = MutableLiveData<Result<UploadDataResponse>>()
        expectedResponse.value = Result.Success(dummyAddStoryResponse)
        Mockito.`when`(repository.uploadLiveStory("bearer", "foto", dummyMockFile))
            .thenReturn(expectedResponse)
        val actualUploadResponse =
            addStoryViewModel.uploadStory("bearer", "foto", dummyMockFile).getOrAwaitValue()
        Mockito.verify(repository).uploadLiveStory("bearer", "foto", dummyMockFile)
        assertNotNull(actualUploadResponse)
        assertTrue(actualUploadResponse is Result.Success)
        assertEquals(dummyAddStoryResponse, (actualUploadResponse as Result.Success).data)

    }

    @Test
    fun `when get marker data success must not null and success`() {
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
    fun `when get story list success must not null and success`() = runTest {
        val dummyListStory = DataDummy.generateDummyStoryResponse()
        val storiesData = PagedDataSource.itemSnapshot(dummyListStory)

        val stories = MutableLiveData<PagingData<Entity>>()
        stories.value = storiesData

        Mockito.`when`(repository.getStoryList()).thenReturn(stories)

        val actualData = repository.getStoryList().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualData)
        advanceUntilIdle()

        Mockito.verify(repository).getStoryList()

        assertNotNull(differ.snapshot())
        assertEquals(DataDummy.generateDummyStoryResponse(), differ.snapshot())
        assertEquals(DataDummy.generateDummyStoryResponse().size, differ.snapshot().size)
        assertEquals(DataDummy.generateDummyStoryResponse()[0].id, differ.snapshot()[0]?.id)
    }
}
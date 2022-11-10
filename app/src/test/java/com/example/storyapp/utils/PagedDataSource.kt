package com.example.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.local_data.Entity

class PagedDataSource:
    PagingSource<Int, LiveData<List<Entity>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Entity>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Entity>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }

    companion object{
        fun itemSnapshot(items: List<Entity>): PagingData<Entity> {
            return PagingData.from(items)
        }
        val listUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
    }
}
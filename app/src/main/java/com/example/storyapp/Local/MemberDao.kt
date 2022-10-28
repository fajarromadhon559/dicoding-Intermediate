package com.example.storyapp.Local

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemberDao {
    @Query("SELECT * FROM member_story ORDER BY createdAt DESC")
    fun getStory(): LiveData<List<Entity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(story: List<Entity>)

    @Query("SELECT * FROM member_story")
    fun getAllStory(): Cursor
}
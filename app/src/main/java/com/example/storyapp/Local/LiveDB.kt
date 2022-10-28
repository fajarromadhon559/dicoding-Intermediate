package com.example.storyapp.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entity::class], version = 1)
abstract class LiveDB : RoomDatabase(){

    abstract fun storyDao() : MemberDao

    companion object{
        @Volatile
        private var instance : LiveDB? = null
        fun getInstance(context: Context) : LiveDB =
            instance ?: synchronized(this){
                instance?: Room.databaseBuilder(
                    context.applicationContext, LiveDB::class.java, "story.db"
                ).build()
            }
    }
}
/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

// declaring dao

@Dao
interface VideoDatabaseDao {

    @Query("select * from databasevideo")

    fun getVideos() : LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVideos(vararg videos: DatabaseVideo)

}

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    abstract val videoDao : VideoDatabaseDao
}

private lateinit var INSTANCE : VideosDatabase
fun getDatabaseInstance(context : Context) : VideosDatabase{
    if(!::INSTANCE.isInitialized) {
        synchronized(VideosDatabase::class.java) {
            // Room.databaseBuilder is the one who manages extending this abstract class and
            // initializing the dao on this class
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    VideosDatabase::class.java,
                    "videos_database") .build()
        }
    }
    return INSTANCE
}
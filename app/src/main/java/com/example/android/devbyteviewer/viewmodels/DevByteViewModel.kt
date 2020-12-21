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

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.android.devbyteviewer.database.getDatabaseInstance
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class DevByteViewModel(application: Application) : AndroidViewModel(application) {


    private val database = getDatabaseInstance(application.applicationContext)

    private val repository = VideosRepository(database)

    val playlist = repository.videos

    init {
        refreshRepository()
    }

    fun refreshRepository() {
        viewModelScope.launch {
            try {
                repository.refreshVideosList()
            }
            catch(e : HttpException) {
                Toast.makeText(getApplication() , "An error occurred!! Try again" , Toast.LENGTH_SHORT).show()
            }
            catch(e : Exception) {
                Toast.makeText(getApplication() , "No Internet" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

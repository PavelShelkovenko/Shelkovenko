package com.pavelshelkovenko.shelkovenko.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pavelshelkovenko.shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.shelkovenko.data.local.models.CacheEntity
import com.pavelshelkovenko.shelkovenko.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    apiService: ApiService,
    db: AppDatabase
): ViewModel() {

    init {
        viewModelScope.launch {
            try {
                val result2 = apiService.getTopFilms()

                val response = Gson().toJson(result2)
                Log.d("MainViewModel", response)

                val cachedao = db.cacheDao()

                cachedao.putCache(
                    CacheEntity(
                        apiRequest = ApiService.TOP_100_POPULAR_FILMS_REQUEST,
                        response = response
                    )
                )

                //val result2 = apiService.searchFilm("ма")
                Log.d("MainViewModel", "${result2.films?.size}")
            } catch(e: Exception) {
                Log.d("MainViewModel", " ${e.message}")
            }
        }
    }
}
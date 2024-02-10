package com.pavelshelkovenko.shelkovenko.data.repository

import com.google.gson.Gson
import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.CacheDao
import com.pavelshelkovenko.shelkovenko.data.local.models.CacheEntity
import com.pavelshelkovenko.shelkovenko.data.remote.ApiService
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDetailsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmsResponse
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.models.Film
import com.pavelshelkovenko.shelkovenko.domain.models.FilmDetails
import retrofit2.HttpException

class FilmsRepositoryImpl(
    private val apiService: ApiService,
    private val cacheDao: CacheDao,
    private val mapper: FilmMapper
) : FilmsRepository {
    override suspend fun getPopularFilms(): Result<List<Film>> {
        return try {
            getTopFilmsFromNetwork()
        } catch (ex: HttpException) {
            getTopFilmsFromCache(cacheDao)
        } catch (ex: Exception) {
            getTopFilmsFromCache(cacheDao)

        }
    }

    override suspend fun getFilmDetails(filmId: Int): Result<FilmDetails> {
        return try {
            getFilmDetailsFromNetwork(filmId)
        } catch (ex: HttpException) {
            getFilmDetailsFromCache(cacheDao, filmId)
        } catch (ex: Exception) {
            getFilmDetailsFromCache(cacheDao, filmId)
        }
    }

    override suspend fun searchFilmByKeyword(keyword: String): Result<List<Film>> {
        TODO("Not yet implemented")
    }

    private suspend fun getFilmDetailsFromNetwork(filmId: Int): Result<FilmDetails> {
        val response = apiService.getFilmDetails(id = filmId)
        val result = mapper.mapFilmDetailsDtoToDomain(response)
        return Result.success(result)
    }

    private suspend fun getFilmDetailsFromCache(cacheDao: CacheDao, filmId: Int): Result<FilmDetails> {
        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$filmId"
        val cachedResponse = cacheDao.getCache(requestPath)
            ?: return Result.failure(IllegalStateException("Empty cache"))
        val response = Gson().fromJson(cachedResponse.response, FilmDetailsResponse::class.java)
        val result = mapper.mapFilmDetailsDtoToDomain(response)
        return Result.success(result)
    }

    private suspend fun getTopFilmsFromNetwork(): Result<List<Film>> {
        val response = apiService.getTopFilms()
        val resultList = response.films?.map { filmDto ->
            mapper.mapDtoToDomain(filmDto)
        }
        return resultList?.let {
            cacheDao.putCache(
                CacheEntity(
                    apiRequest = ApiService.TOP_100_POPULAR_FILMS_REQUEST,
                    response = Gson().toJson(response)
                )
            )
            Result.success(resultList)
        } ?: Result.failure(IllegalStateException("Empty response"))
    }

    private suspend fun getTopFilmsFromCache(cacheDao: CacheDao): Result<List<Film>> {
        val cachedResponse = cacheDao.getCache(ApiService.TOP_100_POPULAR_FILMS_REQUEST)
            ?: return Result.failure(IllegalStateException("Empty cache"))
        val response = Gson().fromJson(cachedResponse.response, FilmsResponse::class.java)
        val resultList = response.films?.map { filmDto ->
            mapper.mapDtoToDomain(filmDto)
        }
        return resultList?.let {
            Result.success(resultList)
        } ?: Result.failure(IllegalStateException("Empty response"))
    }
}
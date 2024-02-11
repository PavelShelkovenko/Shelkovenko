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
    private val mapper: FilmMapper,
    private val gson: Gson
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
        return try {
            val response = apiService.searchFilm(keyword = keyword)
            val resultList = response.films?.let {
                mapper.mapListDtoToDomainList(it)
            }
            return resultList?.let {
                Result.success(resultList)
            } ?: Result.failure(IllegalStateException("Empty response"))
        } catch (ex: HttpException) {
            Result.failure(exception = ex)
        } catch (ex: Exception) {
            Result.failure(exception = ex)
        }
    }

    private suspend fun getFilmDetailsFromNetwork(filmId: Int): Result<FilmDetails> {
        val response = apiService.getFilmDetails(id = filmId)
        val result = mapper.mapFilmDetailsDtoToDomain(response)
        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$filmId"
        cacheDao.putCache(
            CacheEntity(
                apiRequest = requestPath,
                response = gson.toJson(response)
            )
        )
        return Result.success(result)
    }

    private suspend fun getFilmDetailsFromCache(cacheDao: CacheDao, filmId: Int): Result<FilmDetails> {
        val requestPath = "${ApiService.BASE_URL}${ApiService.FILMS_PATH}/$filmId"
        val cachedResponse = cacheDao.getCache(requestPath)
            ?: return Result.failure(IllegalStateException("Empty cache"))
        val response = try {
            gson.fromJson(cachedResponse.response, FilmDetailsResponse::class.java)
        } catch (ex: Exception) {
            null
        }
        response?.let {
            val result = mapper.mapFilmDetailsDtoToDomain(it)
            return Result.success(result)
        } ?: return Result.failure(exception = IllegalStateException("Wrong json"))
    }

    private suspend fun getTopFilmsFromNetwork(): Result<List<Film>> {
        val response = apiService.getTopFilms()
        val resultList = response.films?.let {
            mapper.mapListDtoToDomainList(it)
        }
        return resultList?.let {
            cacheDao.putCache(
                CacheEntity(
                    apiRequest = ApiService.TOP_100_POPULAR_FILMS_REQUEST,
                    response = gson.toJson(response)
                )
            )
            Result.success(resultList)
        } ?: Result.failure(IllegalStateException("Empty response"))
    }

    private suspend fun getTopFilmsFromCache(cacheDao: CacheDao): Result<List<Film>> {
        val cachedResponse = cacheDao.getCache(ApiService.TOP_100_POPULAR_FILMS_REQUEST)
            ?: return Result.failure(IllegalStateException("Empty cache"))
        val response = try {
            gson.fromJson(cachedResponse.response, FilmsResponse::class.java)
        } catch (ex: Exception) {
            null
        }
        val resultList = response?.films?.let {
            mapper.mapListDtoToDomainList(it)
        }
        return resultList?.let {
            Result.success(resultList)
        } ?: Result.failure(IllegalStateException("Empty response"))
    }
}
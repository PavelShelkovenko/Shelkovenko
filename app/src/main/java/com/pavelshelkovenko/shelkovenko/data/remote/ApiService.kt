package com.pavelshelkovenko.shelkovenko.data.remote

import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmDetailsResponse
import com.pavelshelkovenko.shelkovenko.data.remote.models.FilmsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Интерфейс для получения данных из сети
 *
 * @author Shelkovenko Pavel
 */

interface ApiService {

    @GET("$FILMS_PATH/top")
    @Headers("x-api-key: $API_KEY")
    suspend fun getTopFilms(
        @Query("type") type: String = "TOP_100_POPULAR_FILMS"
    ): FilmsResponse

    @GET("$FILMS_PATH/{id}")
    @Headers("x-api-key: $API_KEY")
    suspend fun getFilmDetails(@Path("id") id: Int): FilmDetailsResponse

    @GET(SEARCH_FILMS_PATH)
    @Headers("x-api-key: $API_KEY")
    suspend fun searchFilm(
        @Query("keyword") keyword: String
    ): FilmsResponse

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech"
        const val FILMS_PATH = "/api/v2.2/films"
        const val TOP_100_POPULAR_FILMS_REQUEST = "$BASE_URL$FILMS_PATH/top?type=TOP_100_POPULAR_FILMS"
        const val SEARCH_FILMS_PATH = "/api/v2.1/films/search-by-keyword"
        const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    }
}
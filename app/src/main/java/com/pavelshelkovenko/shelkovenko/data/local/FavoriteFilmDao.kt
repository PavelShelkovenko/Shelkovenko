package com.pavelshelkovenko.shelkovenko.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putFavoriteFilm(film: FavoriteFilmEntity)

    @Query("DELETE FROM favorite_films WHERE id = :filmId")
    suspend fun deleteFavoriteFilm(filmId: Int)

    @Query("SELECT * FROM favorite_films")
    fun getFavoriteFilmsFlow(): Flow<List<FavoriteFilmEntity>>

    @Query("SELECT * FROM favorite_films")
    suspend fun getFavoriteFilms(): List<FavoriteFilmEntity>?

    @Query("SELECT * FROM favorite_films WHERE title LIKE '%' || :query || '%'")
    suspend fun searchFavoriteFilms(query: String): List<FavoriteFilmEntity>?
}
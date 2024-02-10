package com.pavelshelkovenko.shelkovenko.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putFavoriteFilm(film: FavoriteFilmEntity)

    @Delete
    suspend fun deleteFavoriteFilm(film: FavoriteFilmEntity)

    @Query("SELECT * FROM favorite_films")
    fun getFavoriteFilms(): Flow<List<FavoriteFilmEntity>>
}
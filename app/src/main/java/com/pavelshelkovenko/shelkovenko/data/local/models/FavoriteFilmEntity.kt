package com.pavelshelkovenko.shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelshelkovenko.shelkovenko.data.local.AppDatabase

@Entity(tableName = AppDatabase.FAVORITE_FILM_TABLE_NAME)
data class FavoriteFilmEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val year: Int,
    val posterUrl: String,
    val genre: String,
    val countries: String
)
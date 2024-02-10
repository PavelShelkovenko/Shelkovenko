package com.pavelshelkovenko.shelkovenko.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavelshelkovenko.shelkovenko.data.local.models.CacheEntity
import com.pavelshelkovenko.shelkovenko.data.local.models.FavoriteFilmEntity


@Database(
    entities = [FavoriteFilmEntity::class, CacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteFilmDao(): FavoriteFilmDao
    abstract fun cacheDao(): CacheDao

    companion object {

        const val FAVORITE_FILM_TABLE_NAME = "favorite_films"
        const val CACHE_TABLE_NAME = "cache"
        const val DB_NAME = "shelkovenko.db"
    }
}
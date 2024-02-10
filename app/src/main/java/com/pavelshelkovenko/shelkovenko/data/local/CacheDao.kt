package com.pavelshelkovenko.shelkovenko.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavelshelkovenko.shelkovenko.data.local.models.CacheEntity

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putCache(cacheEntity: CacheEntity)

    @Query("SELECT * FROM cache WHERE apiRequest = :request LIMIT 1")
    suspend fun getCache(request: String): CacheEntity
}
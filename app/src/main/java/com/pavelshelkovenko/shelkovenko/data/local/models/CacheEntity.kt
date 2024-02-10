package com.pavelshelkovenko.shelkovenko.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pavelshelkovenko.shelkovenko.data.local.AppDatabase

@Entity(tableName = AppDatabase.CACHE_TABLE_NAME)
data class CacheEntity(
    @PrimaryKey
    val apiRequest: String,
    val response: String
)
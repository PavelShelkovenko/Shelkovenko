package com.pavelshelkovenko.shelkovenko.data.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GenreDto(
    @SerializedName("genre")
    val genre: String?
): Serializable
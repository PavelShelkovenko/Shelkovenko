package com.pavelshelkovenko.shelkovenko.data.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FilmDto(
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("nameRu")
    val title: String?,
    @SerializedName("posterUrlPreview")
    val posterUrl: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    @SerializedName("countries")
    val countries: List<CountryDto>?
): Serializable

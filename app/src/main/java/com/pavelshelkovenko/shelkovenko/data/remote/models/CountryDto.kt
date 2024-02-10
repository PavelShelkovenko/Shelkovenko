package com.pavelshelkovenko.shelkovenko.data.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CountryDto(
    @SerializedName("country")
    val country: String?
): Serializable
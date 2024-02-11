package com.pavelshelkovenko.shelkovenko.presentation.film_details

import com.pavelshelkovenko.shelkovenko.presentation.model.FilmDetailsUi
import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi

sealed interface FilmDetailsScreenState {

    data object Loading: FilmDetailsScreenState

    data class Content(
        val filmDetails: FilmDetailsUi
    ): FilmDetailsScreenState

    data object Error: FilmDetailsScreenState
}
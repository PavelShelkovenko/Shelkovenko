package com.pavelshelkovenko.shelkovenko.presentation.popular_films

import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi


sealed interface PopularFilmsScreenState {

    data object Loading: PopularFilmsScreenState

    data class Content(
        val films: List<FilmUi>
    ) : PopularFilmsScreenState

    data object Error: PopularFilmsScreenState
}

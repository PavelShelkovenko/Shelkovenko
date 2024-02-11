package com.pavelshelkovenko.shelkovenko.presentation.favorite_films

import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi

sealed interface FavoriteFilmsScreenState {

    data object Loading: FavoriteFilmsScreenState

    data class Content(
        val favoriteFilms: List<FilmUi>
    ): FavoriteFilmsScreenState

    data object Error: FavoriteFilmsScreenState
}
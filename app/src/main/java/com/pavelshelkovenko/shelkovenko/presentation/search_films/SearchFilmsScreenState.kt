package com.pavelshelkovenko.shelkovenko.presentation.search_films

import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi

data class SearchFilmsScreenState(
    val keyword: String = "",
    val films: List<FilmUi> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isInitial: Boolean = true,
    val isEmptySearch: Boolean = false
)
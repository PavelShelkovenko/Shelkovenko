package com.pavelshelkovenko.shelkovenko.presentation.search_films

import com.pavelshelkovenko.shelkovenko.presentation.model.FilmUi

//sealed interface SearchFilmsScreenState {
//
//    val keyword: String
//
//    data class Loading(override val keyword: String) : SearchFilmsScreenState
//
//    data class Content(
//        override val keyword: String,
//        val films: List<FilmUi>
//    ): SearchFilmsScreenState
//
//    data class EmptySearchResult(
//        override val keyword: String
//    ): SearchFilmsScreenState
//    data class Error(
//        override val keyword: String
//    ): SearchFilmsScreenState
//
//}

data class SearchFilmsScreenState(
    val keyword: String = "",
    val films: List<FilmUi> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isInitial: Boolean = true,
    val isEmptySearch: Boolean = false
)
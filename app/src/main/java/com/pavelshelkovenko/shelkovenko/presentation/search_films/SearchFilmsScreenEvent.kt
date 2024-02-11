package com.pavelshelkovenko.shelkovenko.presentation.search_films

sealed class SearchFilmsScreenEvent {

    data class ChangeKeyword(val newKeyword: String): SearchFilmsScreenEvent()

    data class DefineSearchArea(val area: SearchArea): SearchFilmsScreenEvent()

    data object Repeat: SearchFilmsScreenEvent()
}
package com.pavelshelkovenko.shelkovenko.presentation.search_films

enum class SearchArea {
    Populars, Favorites;
}

fun getSearchAreaFromInt(area: Int): SearchArea? {
    return when(area) {
        SearchArea.Populars.ordinal -> SearchArea.Populars
        SearchArea.Favorites.ordinal -> SearchArea.Favorites
        else -> null
    }
}
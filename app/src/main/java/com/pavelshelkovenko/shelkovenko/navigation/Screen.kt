package com.pavelshelkovenko.shelkovenko.navigation

sealed class Screen(
    val route: String
) {
    object PopularFilmsScreen : Screen(ROUTE_POPULAR_FILMS_SCREEN)
    object FavoriteFilmsScreen : Screen(ROUTE_FAVORITE_FILMS_SCREEN)
    object SearchFilmsScreen : Screen(ROUTE_SEARCH_FILMS_SCREEN) {
        private const val ROUTE_FOR_ARGS = "search_films_screen"
        fun getRouteWithArgs(area: Int): String {
            return "$ROUTE_FOR_ARGS/$area"
        }
    }
    object FilmsDetailsScreen: Screen(ROUTE_FILMS_DETAILS_SCREEN) {

        private const val ROUTE_FOR_ARGS = "film_details_screen"
        fun getRouteWithArgs(id: Int): String {
            return "$ROUTE_FOR_ARGS/$id"
        }
    }

    companion object {

        const val KEY_FILM_ID = "film_id"
        const val KEY_AREA = "area"

        const val ROUTE_POPULAR_FILMS_SCREEN = "popular_films_screen"
        const val ROUTE_FAVORITE_FILMS_SCREEN = "favorite_films_screen"
        const val ROUTE_SEARCH_FILMS_SCREEN = "search_films_screen/{$KEY_AREA}"

        const val ROUTE_FILMS_DETAILS_SCREEN = "film_details_screen/{$KEY_FILM_ID}"
    }
}
package com.pavelshelkovenko.shelkovenko.presentation

import androidx.compose.runtime.Composable
import com.pavelshelkovenko.shelkovenko.navigation.AppNavGraph
import com.pavelshelkovenko.shelkovenko.navigation.Screen
import com.pavelshelkovenko.shelkovenko.navigation.rememberNavigationState
import com.pavelshelkovenko.shelkovenko.presentation.favorite_films.FavoriteFilmsScreen
import com.pavelshelkovenko.shelkovenko.presentation.film_details.FilmDetailsScreen
import com.pavelshelkovenko.shelkovenko.presentation.popular_films.PopularFilmsScreen
import com.pavelshelkovenko.shelkovenko.presentation.search_films.SearchArea
import com.pavelshelkovenko.shelkovenko.presentation.search_films.SearchFilmsScreen
import com.pavelshelkovenko.shelkovenko.presentation.search_films.getSearchAreaFromInt

@Composable
fun RootScreen() {

    val navigationState = rememberNavigationState()

    AppNavGraph(
        navHostController = navigationState.navHostController,
        popularFilmsScreenContent = {
            PopularFilmsScreen(
                onNavigateToFilmDetails = { filmId ->
                    navigationState.navigateToFilmDetailsScreen(id = filmId)
                },
                onNavigateToFavoriteFilms = {
                    navigationState.navigateTo(Screen.ROUTE_FAVORITE_FILMS_SCREEN)
                },
                onNavigateToSearchFilms = { area ->
                    navigationState.navigateToSearchScreen(area = area.ordinal)
                }
            )
        },
        favoriteFilmsScreenContent = {
            FavoriteFilmsScreen(
                onNavigateToPopularFilms = {
                    navigationState.navigateTo(Screen.ROUTE_POPULAR_FILMS_SCREEN)
                },
                onNavigateToSearchFilms = { area ->
                    navigationState.navigateToSearchScreen(area = area.ordinal)
                },
                onNavigateToFilmDetails = { filmId ->
                    navigationState.navigateToFilmDetailsScreen(id = filmId)
                }
            )
        },
        filmDetailsScreenContent = { id ->
            FilmDetailsScreen(filmId = id) {
                navigationState.navigateBack()
            }
        },
        searchFilmsScreenContent = { area ->
            SearchFilmsScreen(
                area = getSearchAreaFromInt(area = area) ?: SearchArea.Populars,
                onBackPressed = { navigationState.navigateBack() },
                onNavigateToFilmDetails = { filmId ->
                    navigationState.navigateToFilmDetailsScreen(id = filmId)
                }
            )
        }
    )
}
package com.pavelshelkovenko.shelkovenko.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pavelshelkovenko.shelkovenko.presentation.search_films.SearchArea

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    popularFilmsScreenContent: @Composable () -> Unit,
    favoriteFilmsScreenContent: @Composable () -> Unit,
    searchFilmsScreenContent: @Composable (Int) -> Unit,
    filmDetailsScreenContent: @Composable (Int) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.PopularFilmsScreen.route
    ) {
        composable(
            route = Screen.PopularFilmsScreen.route,
        ) {
            popularFilmsScreenContent()
        }
        composable(
            route = Screen.FavoriteFilmsScreen.route,
        ) {
            favoriteFilmsScreenContent()
        }
        composable(
            route = Screen.FilmsDetailsScreen.route,
            arguments = listOf(
                navArgument(Screen.KEY_FILM_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt(Screen.KEY_FILM_ID) ?: 0
            filmDetailsScreenContent(id)
        }
        composable(
            route = Screen.SearchFilmsScreen.route,
            arguments = listOf(
                navArgument(Screen.KEY_AREA) {
                    type = NavType.IntType
                }
            )
        ) {
            val area = it.arguments?.getInt(Screen.KEY_AREA) ?: 0
            searchFilmsScreenContent(area)
        }
    }
}
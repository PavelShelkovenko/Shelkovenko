package com.pavelshelkovenko.shelkovenko.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    popularFilmsScreenContent: @Composable () -> Unit,
    favoriteFilmsScreenContent: @Composable () -> Unit,
    searchFilmsScreenContent: @Composable () -> Unit,
    filmDetailsScreenContent: @Composable (Int) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.PopularFilmsScreen.route
    ) {
        composable(
            route = Screen.PopularFilmsScreen.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            popularFilmsScreenContent()
        }
        composable(
            route = Screen.FavoriteFilmsScreen.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
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
    }
}
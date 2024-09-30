package com.oscorella.cafeyn.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oscorella.cafeyn.home.HomeScreen
import com.oscorella.cafeyn.interests.presentation.InterestsScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {

    SharedTransitionLayout {
        NavHost(navController, startDestination = Screen.Home) {
            composable<Screen.Home> {
                HomeScreen(
                    onNavigateToInterests = {
                        navController.navigate(
                            Screen.Interests
                        )
                    },
                    animatedVisibilityScope = this
                )
            }
            composable<Screen.Interests>(
            ) {
                InterestsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

sealed interface Screen {

    @Serializable
    data object Home: Screen

    @Serializable
    data object Interests : Screen

}
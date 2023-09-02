package bd.emon.movies.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bd.emon.movies.R
import bd.emon.movies.ui.home.HomeRoute

@Composable
fun MovieNavHost(navHostController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { MovieBottomBar(navHostController = navHostController) }
    ) { padding ->
        NavHost(
            navController = navHostController,
            Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeRoute()
            }
            composable(Screen.Trending.route) {
                // HomeScreen()
            }
            composable(Screen.Search.route) {
                // HomeScreen()
            }
            composable(Screen.Favourite.route) {
                // HomeScreen()
            }
        }
    }
}

@Composable
fun MovieBottomBar(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    BottomNavigation {
        for (screen in screens) {
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navHostController.navigate(screen.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(screen.icon, stringResource(id = screen.title)) },
                label = { Text(text = stringResource(id = screen.title)) }
            )
        }
    }
}


val screens = listOf(
    Screen.Home,
    Screen.Trending,
    Screen.Search,
    Screen.Favourite
)

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val title: Int
) {
    object Home : Screen(
        route = "${R.string.home_tab}",
        icon = Icons.Filled.Home,
        title = R.string.home_tab
    )

    object Trending : Screen(
        route = "${R.string.trending_tab}",
        icon = Icons.Filled.TrendingUp,
        title = R.string.trending_tab
    )

    object Search : Screen(
        route = "${R.string.search_tab}",
        icon = Icons.Filled.Search,
        title = R.string.search_tab
    )

    object Favourite : Screen(
        route = "${R.string.favourite_tab}",
        icon = Icons.Filled.Star,
        title = R.string.favourite_tab
    )
}


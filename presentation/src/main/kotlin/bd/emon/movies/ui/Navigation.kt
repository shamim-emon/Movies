package bd.emon.movies.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.movies.R
import bd.emon.movies.ui.home.HomeRoute

@Composable
fun MovieNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    discoverMovieMapper: DiscoverMovieMapper
) {
    Scaffold(
        modifier = modifier,
        bottomBar = { MovieBottomBar(navHostController = navHostController) }
    ) { padding ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeRoute(discoverMovieMapper = discoverMovieMapper)
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
fun MovieBottomBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    NavigationBar(modifier = modifier) {
        for (screen in screens) {
            NavigationBarItem(
                icon = { Icon(screen.icon, stringResource(id = screen.title)) },
                label = {
                    Text(
                        text = stringResource(id = screen.title),
                        style = MaterialTheme.typography.titleSmall

                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {}
            )
        }

    }
}


private val screens = listOf(
    Screen.Home,
    Screen.Trending,
    Screen.Search,
    Screen.Favourite
)

private sealed class Screen(
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


package bd.emon.movies

import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.ActivityMainBinding
import bd.emon.movies.ui.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var screensNavigator: ScreensNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme {
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    val nc = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = stringResource(id = R.string.home_tab)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            imageVector = Icons.Filled.FilterListOff,
                            contentDescription = stringResource(id = R.string.home_tab)
                        )
                    }
                }
            )
        },
        bottomBar = { MovieBottomBar(nc = nc )}
    ) { padding ->
        NavHost(navController = nc, Screen.Home.route, modifier = Modifier.padding(padding)) {
            composable(Screen.Home.route) {
                //HomeScreen()
            }
            composable(Screen.Trending.route) {
                //HomeScreen()
            }
            composable(Screen.Search.route) {
                //HomeScreen()
            }
            composable(Screen.Favourite.route) {
                //HomeScreen()
            }

        }
    }
}

@Composable
fun MovieBottomBar(nc: NavHostController) {
    val currentBackStackEntry by nc.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    BottomNavigation {
        for (screen in screens) {
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    nc.navigate(screen.route) {
                        popUpTo(nc.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(screen.icon, screen.title) },
                label = { Text(screen.title) }
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
    val route:String,
    val icon: ImageVector,
    val title:String
){
    object Home:Screen(
        route = "Home",
        icon = Icons.Filled.Home,
        title = "Home"
    )

    object Trending:Screen(
        route = "Trending",
        icon = Icons.Filled.TrendingUp,
        title = "Trending"
    )

    object Search:Screen(
        route = "Search",
        icon = Icons.Filled.Search,
        title = "Search"
    )

    object Favourite:Screen(
        route = "Favourite",
        icon = Icons.Filled.Star,
        title = "Favourite"
    )


}


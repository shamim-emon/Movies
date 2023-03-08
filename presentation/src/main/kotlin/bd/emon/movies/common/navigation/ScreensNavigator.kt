package bd.emon.movies.common.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.ui.setupWithNavController
import bd.emon.domain.navigation.NavDirectionLabel
import bd.emon.movies.di.entryPoint.ActivityEntryPoint
import bd.emon.movies.favourite.FavouriteFragmentDirections
import bd.emon.movies.home.HomeFragmentDirections
import bd.emon.movies.movieEntity.APICallType
import bd.emon.movies.movieEntity.MovieEntityListFragmentDirections
import bd.emon.movies.search.SearchFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.EntryPointAccessors
import java.io.Serializable

class ScreensNavigator(
    val activity: AppCompatActivity
) :
    Serializable {

    private var navController: NavController

    private var activityEntryPoint: ActivityEntryPoint =
        EntryPointAccessors.fromActivity(activity, ActivityEntryPoint::class.java)

    init {
        navController = activityEntryPoint.navController()
    }

    fun setUpWithNavController(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setupWithNavController(navController)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToSeeMoreList(pageTitle: String, genreId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToSeeMoreListFragment(
            genreId,
            pageTitle,
            APICallType.DISCOVER_PAGING
        )
        navController.navigate(action)
    }

    fun navigateToMovieDetails(id: String, navDirectionLabel: NavDirectionLabel) {
        val direction: NavDirections = provideNavDirections(navDirectionLabel, id)
        navController.navigate(direction)
    }

    private fun provideNavDirections(
        navDirectionLabel: NavDirectionLabel,
        id: String
    ): NavDirections {
        return when (navDirectionLabel) {
            NavDirectionLabel.HomeFragment -> {
                HomeFragmentDirections.actionGlobalMovieDetailsActivity(id)
            }
            NavDirectionLabel.TrendingFragment -> {
                MovieEntityListFragmentDirections.actionMovieEntityListFragmentToMovieDetailsActivity2(
                    id
                )
            }
            NavDirectionLabel.SearchFragment -> {
                SearchFragmentDirections.actionSearchFragmentToMovieDetailsActivity3(id)
            }

            NavDirectionLabel.FavouriteFragment -> {
                FavouriteFragmentDirections.actionFavouriteFragmentToMovieDetailsActivity4(id)
            }
            else -> throw RuntimeException("Invalid Fragment")
        }
    }
}

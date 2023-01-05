package bd.emon.movies.common.navigation

import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import bd.emon.movies.MainActivity
import bd.emon.movies.di.entryPoint.ActivityEntryPoint
import bd.emon.movies.home.HomeFragmentDirections
import bd.emon.movies.movieEntity.APICallType
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.EntryPointAccessors
import java.io.Serializable

class ScreensNavigator(
    activity: MainActivity
) :
    Serializable {
    private var navController: NavController
    private var activityEntryPoint: ActivityEntryPoint

    init {
        activityEntryPoint =
            EntryPointAccessors.fromActivity(activity, ActivityEntryPoint::class.java)
        navController = activityEntryPoint.navController()
    }

    fun setUpWithNavController(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setupWithNavController(navController)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToSeeMoreList(pageTitle: String, genreId: Int, genre: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToSeeMoreListFragment(
            genreId,
            pageTitle,
            APICallType.DISCOVER_PAGING
        )
        navController.navigate(action)
    }
}

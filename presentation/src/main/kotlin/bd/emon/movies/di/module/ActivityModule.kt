package bd.emon.movies.di.module

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import bd.emon.movies.R
import bd.emon.movies.home.MovieReleaseYearsProvider
import bd.emon.movies.home.MovieReleaseYearsProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.Calendar

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun provideActivityCompat(activity: Activity): AppCompatActivity = activity as AppCompatActivity

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity) = activity.supportFragmentManager

    @Provides
    fun provideNavHostContainer() = R.id.nav_host_container

    @Provides
    fun provideNavHostFragment(
        fragmentManager: FragmentManager,
        navHostContainerId: Int
    ): NavHostFragment =
        fragmentManager.findFragmentById(navHostContainerId) as NavHostFragment

    @Provides
    fun provideNavController(navHostFragment: NavHostFragment) = navHostFragment.navController

    @Provides
    fun provideCalender() = Calendar.getInstance()

    @Provides
    fun provideMovieReleaseYearsProvider(calendar: Calendar): MovieReleaseYearsProvider =
        MovieReleaseYearsProviderImpl(calendar)

    @Provides
    fun provideReleaseYearsList(movieReleaseProvider: MovieReleaseYearsProvider) =
        movieReleaseProvider.getReleaseYears()
}
package bd.emon.movies.di.module

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import bd.emon.movies.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

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
}

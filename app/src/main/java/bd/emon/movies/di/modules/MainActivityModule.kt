package bd.emon.movies.di.modules

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import bd.emon.movies.R
import bd.emon.movies.databinding.ActivityMainBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {
    @Provides
    fun provideActivityCompat(activity: Activity): AppCompatActivity = activity as AppCompatActivity

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity) = activity.supportFragmentManager

    @Provides
    fun containerId() = R.id.fragmentContainer

    @Provides
    fun provideMainActivityBinding(activity: Activity) =
        ActivityMainBinding.inflate(activity.layoutInflater)
}

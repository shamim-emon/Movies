package bd.emon.movies.di.module

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import bd.emon.movies.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    fun provideContainerId() = R.id.fragmentContainer

    @Provides
    fun provideDialogBuilder(activity: AppCompatActivity) = MaterialAlertDialogBuilder(activity)
}

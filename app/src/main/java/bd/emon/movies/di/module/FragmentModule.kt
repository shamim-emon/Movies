package bd.emon.movies.di.module

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import bd.emon.movies.R
import bd.emon.movies.home.FilterDialogBindingHelper
import bd.emon.movies.home.FilterDialogBindingHelperImpl
import bd.emon.movies.home.HomePatchAdapterViewHolderContainer
import bd.emon.movies.home.HomePatchAdapterViewHolderContainerImpl
import bd.emon.movies.home.HomePatchAdapterViewHolderOnScreenDataHolder
import bd.emon.movies.home.HomePatchAdapterViewHolderOnScreenDataHolderImpl
import bd.emon.movies.home.MovieReleaseYearsProvider
import bd.emon.movies.home.MovieReleaseYearsProviderImpl
import bd.emon.movies.movieEntity.MovieEntityNavDirectionLabelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import java.util.Calendar

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @Provides
    fun provideHomePatchAdapterViewHolderContainer(): HomePatchAdapterViewHolderContainer =
        HomePatchAdapterViewHolderContainerImpl()

    @Provides
    fun provideHomePatchAdapterViewHolderOnScreenDataHolder(): HomePatchAdapterViewHolderOnScreenDataHolder =
        HomePatchAdapterViewHolderOnScreenDataHolderImpl()

    @Provides
    fun provideCalender() = Calendar.getInstance()

    @Provides
    fun provideMovieReleaseYearsProvider(calendar: Calendar): MovieReleaseYearsProvider =
        MovieReleaseYearsProviderImpl(calendar)

    @Provides
    fun provideReleaseYearsList(movieReleaseProvider: MovieReleaseYearsProvider) =
        movieReleaseProvider.getReleaseYearsList()

    @Provides
    fun getMovieSortingCriteria(activity: Activity) =
        activity.resources.getStringArray(R.array.movie_sorting_criteria).toList()

    @Provides
    fun provideDialogBuilder(activity: AppCompatActivity) =
        MaterialAlertDialogBuilder(activity as Context)

    @Provides
    fun provideFilterDialogBindingHelper(): FilterDialogBindingHelper =
        FilterDialogBindingHelperImpl()

    @Provides
    fun provideInputMethodManager(activity: Activity) =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    fun provideMovieEntityNavDirectionLabelProvider() = MovieEntityNavDirectionLabelProvider()
}

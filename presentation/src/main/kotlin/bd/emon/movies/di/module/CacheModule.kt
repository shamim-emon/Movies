package bd.emon.movies.di.module

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import bd.emon.data.cache.MovieCacheApiInterface
import bd.emon.data.cache.MovieCacheApiInterfaceImpl
import bd.emon.data.cache.MovieCacheRepositoryImpl
import bd.emon.domain.MovieCacheRepository
import bd.emon.domain.SchedulerProvider
import bd.emon.domain.usecase.ClearCacheDiscoverMoviesFiltersUseCase
import bd.emon.domain.usecase.GetCacheDiscoverMovieFilterUseCase
import bd.emon.domain.usecase.SaveCacheDiscoverMoviesFiltersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(
    ViewModelComponent::class
)
object CacheModule {

    @Provides
    fun provideMovieCacheApiInterface(store: RxDataStore<Preferences>): MovieCacheApiInterface =
        MovieCacheApiInterfaceImpl(store)

    @Provides
    fun provideMovieCacheRepository(
        schedulerProvider: SchedulerProvider,
        apiInterface: MovieCacheApiInterface
    ): MovieCacheRepository =
        MovieCacheRepositoryImpl(
            schedulerProvider,
            apiInterface
        )

    @Provides
    fun provideSaveCacheDiscoverMoviesFiltersUseCase(movieCacheRepository: MovieCacheRepository) =
        SaveCacheDiscoverMoviesFiltersUseCase(
            movieCacheRepository
        )

    @Provides
    fun provideGetCacheDiscoverMoviesFiltersUseCase(movieCacheRepository: MovieCacheRepository) =
        GetCacheDiscoverMovieFilterUseCase(
            movieCacheRepository
        )

    @Provides
    fun provideClearCacheDiscoverMoviesFiltersUseCase(movieCacheRepository: MovieCacheRepository) =
        ClearCacheDiscoverMoviesFiltersUseCase(
            movieCacheRepository
        )
}

package bd.emon.movies.di.module

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import bd.emon.movies.cache.MovieCacheApiInterface
import bd.emon.movies.cache.MovieCacheApiInterfaceImpl
import bd.emon.movies.cache.MovieCacheRepository
import bd.emon.movies.cache.MovieCacheRepositoryImpl
import bd.emon.movies.common.ASyncTransformer
import bd.emon.movies.common.DATA_STORE_NAME
import bd.emon.movies.usecase.GetCacheDiscoverMovieFilterUseCase
import bd.emon.movies.usecase.SaveCacheDiscoverMoviesFiltersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(
    ViewModelComponent::class
)
object CacheModule {

    @ViewModelScoped
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context) =
        RxPreferenceDataStoreBuilder(appContext, DATA_STORE_NAME).build()

    @Provides
    fun provideMovieCacheApiInterface(store: RxDataStore<Preferences>): MovieCacheApiInterface =
        MovieCacheApiInterfaceImpl(store)

    @Provides
    fun provideMovieCacheRepository(apiInterface: MovieCacheApiInterface): MovieCacheRepository =
        MovieCacheRepositoryImpl(apiInterface)

    @Provides
    fun provideSaveCacheDiscoverMoviesFiltersUseCase(movieCacheRepository: MovieCacheRepository) =
        SaveCacheDiscoverMoviesFiltersUseCase(
            ASyncTransformer(),
            movieCacheRepository
        )

    @Provides
    fun provideGetCacheDiscoverMoviesFiltersUseCase(movieCacheRepository: MovieCacheRepository) =
        GetCacheDiscoverMovieFilterUseCase(
            ASyncTransformer(),
            movieCacheRepository
        )
}

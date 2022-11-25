package bd.emon.movies.di.module

import bd.emon.movies.common.SchedulerProvider
import bd.emon.movies.common.SchedulerProviderImpl
import bd.emon.movies.rest.MovieRestApiInterface
import bd.emon.movies.rest.MovieRestRepository
import bd.emon.movies.rest.MovieRestRepositoryImpl
import bd.emon.movies.rest.RestApiServiceProvider
import bd.emon.movies.usecase.GetDiscoverMoviesUseCase
import bd.emon.movies.usecase.GetGenresUseCase
import bd.emon.movies.usecase.GetSearchResultUseCase
import bd.emon.movies.usecase.GetTrendingMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object RestModule {
    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create()

    @Provides
    fun provideRxAdapter() = RxJava3CallAdapterFactory.create()

    @Provides
    fun provideRestApiServiceProvider(
        converterFactory: GsonConverterFactory,
        rxAdapter: RxJava3CallAdapterFactory
    ) = RestApiServiceProvider(converterFactory, rxAdapter)

    @Provides
    fun provideTMDBApiService(restApiServiceProvider: RestApiServiceProvider): MovieRestApiInterface =
        restApiServiceProvider.providerTMDBApiService()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    fun provideMovieRepository(
        schedulerProvider: SchedulerProvider,
        tmdbApiInterface: MovieRestApiInterface
    ): MovieRestRepository =
        MovieRestRepositoryImpl(tmdbApiInterface, schedulerProvider)

    @Provides
    fun provideGetGenresUseCase(movieApisImpl: MovieRestRepository): GetGenresUseCase =
        GetGenresUseCase(movieApisImpl)

    @Provides
    fun provideGetDiscoverMoviesUseCase(movieApisImpl: MovieRestRepository): GetDiscoverMoviesUseCase =
        GetDiscoverMoviesUseCase(movieApisImpl)

    @Provides
    fun provideGetTrendingMoviesUseCase(movieApisImpl: MovieRestRepository): GetTrendingMoviesUseCase =
        GetTrendingMoviesUseCase(movieApisImpl)

    @Provides
    fun provideGetGetSearchResultUseCase(movieApisImpl: MovieRestRepository): GetSearchResultUseCase =
        GetSearchResultUseCase(movieApisImpl)
}

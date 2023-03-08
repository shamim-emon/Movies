package bd.emon.movies.di.module

import bd.emon.data.SchedulerProviderImpl
import bd.emon.data.rest.MovieRestApiInterface
import bd.emon.data.rest.MovieRestRepositoryImpl
import bd.emon.data.rest.RestApiServiceProvider
import bd.emon.domain.MovieRestRepository
import bd.emon.domain.SchedulerProvider
import bd.emon.domain.usecase.GetDiscoverMoviesUseCase
import bd.emon.domain.usecase.GetGenresUseCase
import bd.emon.domain.usecase.GetMovieDetailsUseCase
import bd.emon.domain.usecase.GetMovieVideosUseCase
import bd.emon.domain.usecase.GetSearchResultUseCase
import bd.emon.domain.usecase.GetTrendingMoviesUseCase
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
    fun provideGetGenresUseCase(movieRestRepository: MovieRestRepository): GetGenresUseCase =
        GetGenresUseCase(movieRestRepository)

    @Provides
    fun provideGetDiscoverMoviesUseCase(movieRestRepository: MovieRestRepository): GetDiscoverMoviesUseCase =
        GetDiscoverMoviesUseCase(movieRestRepository)

    @Provides
    fun provideGetTrendingMoviesUseCase(movieRestRepository: MovieRestRepository): GetTrendingMoviesUseCase =
        GetTrendingMoviesUseCase(movieRestRepository)

    @Provides
    fun provideGetGetSearchResultUseCase(movieRestRepository: MovieRestRepository): GetSearchResultUseCase =
        GetSearchResultUseCase(movieRestRepository)

    @Provides
    fun provideGetMovieDetailsUseCase(movieRestRepository: MovieRestRepository): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(movieRestRepository)

    @Provides
    fun provideGetMovieVideosUseCase(movieRestRepository: MovieRestRepository) =
        GetMovieVideosUseCase(movieRestRepository)
}

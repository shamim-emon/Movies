package bd.emon.movies.di.module

import bd.emon.movies.common.ASyncTransformer
import bd.emon.movies.rest.MovieApiInterface
import bd.emon.movies.rest.MovieRepository
import bd.emon.movies.rest.MovieRepositoryImpl
import bd.emon.movies.rest.RestApiServiceProvider
import bd.emon.movies.usecase.GetDiscoverMoviesUseCase
import bd.emon.movies.usecase.GetGenresUseCase
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
    fun provideTMDBApiService(restApiServiceProvider: RestApiServiceProvider): MovieApiInterface =
        restApiServiceProvider.providerTMDBApiService()

    @Provides
    fun provideMovieRepository(tmdbApiInterface: MovieApiInterface): MovieRepository =
        MovieRepositoryImpl(tmdbApiInterface)

    @Provides
    fun provideGetGenresUseCase(movieApisImpl: MovieRepository): GetGenresUseCase =
        GetGenresUseCase(ASyncTransformer(), movieApisImpl)

    @Provides
    fun provideGetDiscoverMoviesUseCase(movieApisImpl: MovieRepository): GetDiscoverMoviesUseCase =
        GetDiscoverMoviesUseCase(ASyncTransformer(), movieApisImpl)
}

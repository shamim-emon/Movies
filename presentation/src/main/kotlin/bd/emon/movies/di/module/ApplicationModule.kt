package bd.emon.movies.di.module

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.data.dataMapper.SearchMovieMapper
import bd.emon.data.dataMapper.TrendingMovieMapper
import bd.emon.domain.DATA_STORE_NAME
import bd.emon.domain.view.ViewSizeHelper
import bd.emon.movies.BuildConfig
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context) =
        RxPreferenceDataStoreBuilder(appContext, DATA_STORE_NAME).build()

    @Singleton
    @Provides
    fun provideDiscoverMovieMapper() = DiscoverMovieMapper()

    @Singleton
    @Provides
    fun provideTrendingMovieMapper() = TrendingMovieMapper()

    @Singleton
    @Provides
    fun provideSearchMovieMapper() = SearchMovieMapper()

    @Singleton
    @Provides
    fun provideViewSizeHelper() = ViewSizeHelper()

    @ApiKey
    @Provides
    fun provideApiKey(application: Application) = BuildConfig.API_KEY

    @AppLanguage
    @Provides
    fun provideAppLanguage() = "en-US"
}

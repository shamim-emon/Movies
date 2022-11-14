package bd.emon.movies.di.module

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import bd.emon.movies.R
import bd.emon.movies.common.DATA_STORE_NAME
import bd.emon.movies.common.dataMapper.DiscoverMovieMapper
import bd.emon.movies.common.dataMapper.TrendingMovieMapper
import bd.emon.movies.common.view.ViewResizer
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
    fun provideViewResizer() = ViewResizer()

    @ApiKey
    @Provides
    fun provideApiKey(application: Application) = application.resources.getString(R.string.api_key)

    @AppLanguage
    @Provides
    fun provideAppLanguage() = "en-US"
}

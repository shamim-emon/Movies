package bd.emon.movies.di.module

import android.app.Application
import bd.emon.movies.R
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ApiKey
    @Provides
    fun provideApiKey(application: Application) = application.resources.getString(R.string.api_key)

    @AppLanguage
    @Provides
    fun provideAppLanguage() = "en-US"
}
package bd.emon.movies.di.module

import android.content.Context
import bd.emon.data.room.MovieDb
import bd.emon.data.room.MovieDbRepositoryImpl
import bd.emon.domain.usecase.AddToFavouriteUseCase
import bd.emon.domain.usecase.GetAllFavouritesUseCase
import bd.emon.domain.usecase.GetFavouriteMovieByIdUseCase
import bd.emon.domain.usecase.RemoveFromFavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RoomDbModule {

    @Provides
    fun getRoomDatabase(@ApplicationContext appContext: Context) = MovieDb(appContext)

    @Provides
    fun provideMovieDbRepository(db: MovieDb): bd.emon.domain.MovieDbRepository =
        MovieDbRepositoryImpl(db)

    @Provides
    fun provideAddToFavouriteUseCase(repository: bd.emon.domain.MovieDbRepository) =
        AddToFavouriteUseCase(repository)

    @Provides
    fun provideRemoveFromFavouriteUseCase(repository: bd.emon.domain.MovieDbRepository) =
        RemoveFromFavouriteUseCase(repository)

    @Provides
    fun provideGetAllFavouritesUseCase(repository: bd.emon.domain.MovieDbRepository) =
        GetAllFavouritesUseCase(repository)

    @Provides
    fun provideGetFavouriteMovieByIdUseCase(repository: bd.emon.domain.MovieDbRepository) =
        GetFavouriteMovieByIdUseCase(repository)
}

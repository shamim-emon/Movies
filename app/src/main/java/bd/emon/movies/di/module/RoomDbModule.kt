package bd.emon.movies.di.module

import android.content.Context
import bd.emon.movies.room.MovieDb
import bd.emon.movies.room.MovieDbRepository
import bd.emon.movies.room.MovieDbRepositoryImpl
import bd.emon.movies.usecase.AddToFavouriteUseCase
import bd.emon.movies.usecase.GetAllFavouritesUseCase
import bd.emon.movies.usecase.GetFavouriteMovieByIdUseCase
import bd.emon.movies.usecase.RemoveFromFavouriteUseCase
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
    fun provideMovieDbRepository(db: MovieDb): MovieDbRepository = MovieDbRepositoryImpl(db)

    @Provides
    fun provideAddToFavouriteUseCase(repository: MovieDbRepository) =
        AddToFavouriteUseCase(repository)

    @Provides
    fun provideRemoveFromFavouriteUseCase(repository: MovieDbRepository) =
        RemoveFromFavouriteUseCase(repository)

    @Provides
    fun provideGetAllFavouritesUseCase(repository: MovieDbRepository) =
        GetAllFavouritesUseCase(repository)

    @Provides
    fun provideGetFavouriteMovieByIdUseCase(repository: MovieDbRepository) =
        GetFavouriteMovieByIdUseCase(repository)
}

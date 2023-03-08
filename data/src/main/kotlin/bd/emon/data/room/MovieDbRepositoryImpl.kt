package bd.emon.data.room

import bd.emon.domain.MovieDbRepository
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDbRepositoryImpl(private val db: MovieDb) : MovieDbRepository {

    private val movieDao = db.movieDao()

    override fun addToFavourite(movieEntity: MovieEntity): Observable<Optional<Long>> {
        return movieDao.insertMovie(movieEntity).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map {
                Optional.of(it)
            }.toObservable()
    }

    override fun removeFromFavourite(movieEntity: MovieEntity): Observable<Optional<Int>> {
        return movieDao.deleteMovie(movieEntity).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map {
                Optional.of(it)
            }.toObservable()
    }

    override fun getAllFavourites(): Observable<Optional<List<MovieEntity>>> {
        return movieDao.getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map {
                Optional.of(it)
            }.toObservable()
    }

    override fun getFavouriteMovieById(id: Int): Observable<Optional<MovieEntity>> {
        return movieDao.getFavouriteMovieById(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map {
                Optional.of(it)
            }.toObservable()
    }
}

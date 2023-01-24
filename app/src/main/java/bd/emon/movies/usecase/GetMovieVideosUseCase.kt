package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_MOVIE_ID
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.details.MovieVideos
import bd.emon.movies.rest.MovieRestRepository
import io.reactivex.rxjava3.core.Observable

class GetMovieVideosUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<MovieVideos>>() {
    lateinit var apiKey: String
    lateinit var movieId: String
    fun getMovieVideos(apiKey: String, movieId: String): Observable<Optional<MovieVideos>> {
        this.apiKey = apiKey
        this.movieId = movieId
        val apiParams = hashMapOf<String, Any?>()
        apiParams[PARAM_API_KEY] = this.apiKey
        apiParams[PARAM_MOVIE_ID] = this.movieId
        return observable(apiParams)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MovieVideos>> {
        var throwable: Throwable
        try {
            return movieRestRepository.getMovieVideos(
                withParam?.get(PARAM_MOVIE_ID) as String,
                withParam?.get(PARAM_API_KEY) as String
            )
        } catch (t: Throwable) {
            throwable = t
        }
        return Observable.error(throwable)
    }
}

package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_MOVIE_ID
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.details.MovieDetails
import bd.emon.movies.rest.MovieRestRepository
import io.reactivex.rxjava3.core.Observable

class GetMovieDetailsUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<MovieDetails>>() {
    lateinit var apiKey: String
    lateinit var language: String
    lateinit var movieId: String
    fun getMovieDetails(
        apiKey: String,
        language: String,
        movieId: String
    ): Observable<Optional<MovieDetails>> {
        this.apiKey = apiKey
        this.language = language
        this.movieId = movieId
        val apiParams = hashMapOf<String, Any?>()
        apiParams[PARAM_API_KEY] = this.apiKey
        apiParams[PARAM_LANGUAGE] = this.language
        apiParams[PARAM_MOVIE_ID] = this.movieId
        return observable(apiParams)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MovieDetails>> {
        var throwable: Throwable
        try {
            return movieRestRepository.getMovieDetails(
                withParam?.get(PARAM_API_KEY) as String,
                withParam?.get(PARAM_LANGUAGE) as String,
                withParam?.get(PARAM_MOVIE_ID) as String
            )
        } catch (t: Throwable) {
            throwable = t
        }
        return Observable.error(throwable)
    }
}

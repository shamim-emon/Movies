package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.common.Transformer
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.rest.MovieRepository
import io.reactivex.rxjava3.core.Observable

class GetDiscoverMoviesUseCase(
    transformer: Transformer<Optional<DiscoverMovie>>,
    private val movieRepository: MovieRepository
) : UseCase<Optional<DiscoverMovie>>(transformer) {
    lateinit var params: HashMap<String, Any?>
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<DiscoverMovie>> {
        params = hashMapOf()
        params[PARAM_API_KEY] = withParam!![PARAM_API_KEY]
        params[PARAM_LANGUAGE] = withParam[PARAM_LANGUAGE]
        params[PARAM_GENRES] = withParam[PARAM_GENRES]
        withParam[PARAM_SORT_BY]?.let {
            params[PARAM_SORT_BY] = withParam[PARAM_SORT_BY]
        }
        withParam[PARAM_INCLUDE_ADULT]?.let {
            params[PARAM_INCLUDE_ADULT] = withParam[PARAM_INCLUDE_ADULT]
        }

        withParam[PARAM_PAGE]?.let {
            params[PARAM_PAGE] = withParam[PARAM_PAGE]
        }

        withParam[PARAM_VOTE_COUNT_GREATER_THAN]?.let {
            params[PARAM_VOTE_COUNT_GREATER_THAN] = withParam[PARAM_VOTE_COUNT_GREATER_THAN]
        }

        var throwable: Throwable
        try {
            return movieRepository.getDiscoverMovies(params)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getDiscoverMovies(withParam: HashMap<String, Any?>?): Observable<Optional<DiscoverMovie>> {
        return observable(withParam)
    }
}

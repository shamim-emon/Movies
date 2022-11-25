package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_RELEASE_YEAR
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.discover.DiscoverMovies
import bd.emon.movies.rest.MovieRestRepository
import io.reactivex.rxjava3.core.Observable

class GetDiscoverMoviesUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<DiscoverMovies>>() {
    lateinit var params: HashMap<String, Any?>
    var page: Int = 0
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<DiscoverMovies>> {
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

        withParam[PARAM_RELEASE_YEAR]?.let {
            params[PARAM_RELEASE_YEAR] = it
        }

        var throwable: Throwable
        try {
            return movieRestRepository.getDiscoverMovies(params)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getDiscoverMovies(
        withParam: HashMap<String, Any?>?,
        page: Int
    ): Observable<Optional<DiscoverMovies>> {
        this.page = page
        withParam!![PARAM_PAGE] = this.page
        return observable(withParam)
    }
}

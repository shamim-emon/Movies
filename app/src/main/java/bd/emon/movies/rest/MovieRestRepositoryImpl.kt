package bd.emon.movies.rest

import bd.emon.movies.common.DEBOUNCE_DEAULT_DURATION
import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_RELEASE_YEAR
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.common.SchedulerProvider
import bd.emon.movies.common.toApiParam
import bd.emon.movies.common.toLowerCaseNoSpace
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.details.MovieDetails
import bd.emon.movies.entity.discover.DiscoverMovies
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.entity.search.MovieSearch
import bd.emon.movies.entity.trending.TrendingMovies
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MovieRestRepositoryImpl(
    private val movieRestApiInterface: MovieRestApiInterface,
    private val schedulerProvider: SchedulerProvider
) :
    MovieRestRepository {
    override fun getGenres(withParam: Map<String, Any?>): Observable<Optional<Genres>> {
        val params = HashMap<String, String>()
        params[PARAM_API_KEY.toApiParam()] = withParam[PARAM_API_KEY] as String
        params[PARAM_LANGUAGE.toApiParam()] = withParam[PARAM_LANGUAGE] as String

        return movieRestApiInterface.getGenres(params)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map {
                Optional.of(it)
            }
    }

    override fun getDiscoverMovies(
        withParam: Map<String, Any?>
    ): Observable<Optional<DiscoverMovies>> {
        val params = HashMap<String, String>()
        params[PARAM_API_KEY.toApiParam()] = withParam[PARAM_API_KEY] as String
        params[PARAM_LANGUAGE.toApiParam()] = withParam[PARAM_LANGUAGE] as String
        params[PARAM_GENRES.toApiParam()] = (withParam[PARAM_GENRES] as Int).toString()

        withParam[PARAM_SORT_BY]?.let {
            params[PARAM_SORT_BY.toApiParam()] = (it as String).toLowerCaseNoSpace()
        }

        withParam[PARAM_INCLUDE_ADULT]?.let {
            params[PARAM_INCLUDE_ADULT.toApiParam()] = (it as Boolean).toString()
        } ?: run {
            params[PARAM_INCLUDE_ADULT.toApiParam()] = false.toString()
        }

        withParam[PARAM_PAGE]?.let {
            params[PARAM_PAGE.toApiParam()] = (it as Int).toString()
        }

        withParam[PARAM_VOTE_COUNT_GREATER_THAN]?.let {
            params[PARAM_VOTE_COUNT_GREATER_THAN.toApiParam()] = (it as Int).toString()
        }

        withParam[PARAM_RELEASE_YEAR]?.let {
            params[PARAM_RELEASE_YEAR.toApiParam()] = it as String
        }
        return movieRestApiInterface.getDiscoverMovies(params)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map {
                it.grp_genre_id = withParam[PARAM_GENRES] as Int
                Optional.of(it)
            }
    }

    override fun getTrendingMovies(
        apiKey: String,
        page: Int
    ): Observable<Optional<TrendingMovies>> {
        return movieRestApiInterface.getTrendingMovies(apiKey, page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map {
                Optional.of(it)
            }
    }

    override fun getSearchResult(
        apiKey: String,
        language: String,
        page: Int,
        includeAdult: Boolean,
        query: String
    ): Observable<Optional<MovieSearch>> {
        return Observable.just(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .debounce(DEBOUNCE_DEAULT_DURATION, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                movieRestApiInterface.getSearchResult(
                    apiKey,
                    language,
                    page,
                    includeAdult,
                    it
                ).map { mov ->
                    Optional.of(mov)
                }
            }
    }

    override fun getMovieDetails(
        apiKey: String,
        language: String,
        movieId: String
    ): Observable<Optional<MovieDetails>> {
        TODO()
    }
}

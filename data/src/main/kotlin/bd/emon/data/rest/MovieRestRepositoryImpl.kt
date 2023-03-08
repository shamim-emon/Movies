package bd.emon.data.rest

import bd.emon.data.toApiParam
import bd.emon.data.toLowerCaseNoSpace
import bd.emon.domain.DEBOUNCE_DEAULT_DURATION
import bd.emon.domain.MovieRestRepository
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_LANGUAGE
import bd.emon.domain.PARAM_PAGE
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.domain.SchedulerProvider
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.details.MovieDetails
import bd.emon.domain.entity.details.MovieVideos
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.domain.entity.search.MovieSearch
import bd.emon.domain.entity.trending.TrendingMovies
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

        params[PARAM_INCLUDE_ADULT.toApiParam()] = false.toString()

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
        return movieRestApiInterface.getMovieDetails(
            movieId,
            apiKey,
            language
        ).map { details ->
            Optional.of(details)
        }
    }

    override fun getMovieVideos(
        movieId: String,
        apiKey: String
    ): Observable<Optional<MovieVideos>> {
        return movieRestApiInterface.getMovieVideos(movieId, apiKey).map { videos ->
            Optional.of(videos)
        }
    }
}

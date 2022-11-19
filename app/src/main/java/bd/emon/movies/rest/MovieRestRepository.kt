package bd.emon.movies.rest

import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.discover.DiscoverMovies
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.entity.search.MovieSearch
import bd.emon.movies.entity.trending.TrendingMovies
import io.reactivex.rxjava3.core.Observable

interface MovieRestRepository {
    fun getGenres(withParam: Map<String, Any?>): Observable<Optional<Genres>>
    fun getDiscoverMovies(
        withParam: Map<String, Any?>
    ): Observable<Optional<DiscoverMovies>>

    fun getTrendingMovies(
        apiKey: String,
        page: Int
    ): Observable<Optional<TrendingMovies>>

    fun getSearchResult(
        apiKey: String,
        language: String,
        page: Int,
        includeAdult: Boolean,
        query: String
    ): Observable<Optional<MovieSearch>>
}

package bd.emon.domain

import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.details.MovieDetails
import bd.emon.domain.entity.details.MovieVideos
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.domain.entity.search.MovieSearch
import bd.emon.domain.entity.trending.TrendingMovies
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

    fun getMovieDetails(
        apiKey: String,
        language: String,
        movieId: String
    ): Observable<Optional<MovieDetails>>

    fun getMovieVideos(
        movieId: String,
        apiKey: String
    ): Observable<Optional<MovieVideos>>
}

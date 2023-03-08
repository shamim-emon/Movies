package bd.emon.data.rest

import bd.emon.domain.entity.details.MovieDetails
import bd.emon.domain.entity.details.MovieVideos
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.domain.entity.search.MovieSearch
import bd.emon.domain.entity.trending.TrendingMovies
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieRestApiInterface {
    @GET("genre/movie/list")
    fun getGenres(
        @QueryMap options: Map<String, String>
    ): Observable<Genres>

    @GET("discover/movie")
    fun getDiscoverMovies(
        @QueryMap options: Map<String, String>
    ): Observable<DiscoverMovies>

    @GET("trending/movie/day")
    fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Observable<TrendingMovies>

    @GET("search/movie")
    fun getSearchResult(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean,
        @Query("query") query: String
    ): Observable<MovieSearch>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<MovieDetails>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Observable<MovieVideos>
}

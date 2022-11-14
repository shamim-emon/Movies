package bd.emon.movies.rest

import bd.emon.movies.entity.discover.DiscoverMovies
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.entity.trending.TrendingMovies
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
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
}

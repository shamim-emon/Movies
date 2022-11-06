package bd.emon.movies.rest

import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.entity.genre.Genres
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieRestApiInterface {
    @GET("genre/movie/list")
    fun getGenres(
        @QueryMap options: Map<String, String>
    ): Observable<Genres>

    @GET("discover/movie")
    fun getDiscoverMovies(
        @QueryMap options: Map<String, String>
    ): Observable<DiscoverMovie>
}

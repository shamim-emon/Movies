package bd.emon.movies.rest

import bd.emon.movies.entity.Genres
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("genre/movie/list?")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<Genres>
}

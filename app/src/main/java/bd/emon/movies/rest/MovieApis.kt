package bd.emon.movies.rest

import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.entity.Optional
import io.reactivex.rxjava3.core.Observable

interface MovieApis {
    fun getGenres(apiKey: String, language: String): Observable<Optional<Genres>>
}

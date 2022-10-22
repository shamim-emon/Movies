package bd.emon.movies.rest

import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.entity.genre.Genres
import io.reactivex.rxjava3.core.Observable

interface MovieRepository {
    fun getGenres(withParam: Map<String, Any?>): Observable<Optional<Genres>>
    fun getDiscoverMovies(
        withParam: Map<String, Any?>
    ): Observable<Optional<DiscoverMovie>>
}

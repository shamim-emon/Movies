package bd.emon.movies.rest

import bd.emon.movies.entity.Genres
import bd.emon.movies.entity.Optional
import io.reactivex.rxjava3.core.Observable

class MovieApisImpl(private val movieApiInterface: MovieApiInterface) : MovieApis {
    override fun getGenres(apiKey: String, language: String): Observable<Optional<Genres>> {
        return movieApiInterface.getGenres(apiKey, language).map { Optional.of(it) }
    }
}

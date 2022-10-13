package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.Transformer
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.rest.MovieApis
import io.reactivex.rxjava3.core.Observable

open class GetGenresUseCase(
    transformer: Transformer<Optional<Genres>>,
    private val movieApis: MovieApis
) : UseCase<Optional<Genres>>(transformer) {
    lateinit var apiKey: String
    lateinit var lang: String

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<Genres>> {
        apiKey = withParam!![PARAM_API_KEY] as String
        lang = withParam[PARAM_LANGUAGE] as String
        var throwable: Throwable
        try {
            return movieApis.getGenres(apiKey, lang)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }
}

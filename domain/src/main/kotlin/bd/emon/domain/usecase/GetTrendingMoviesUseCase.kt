package bd.emon.domain.usecase

import bd.emon.domain.MovieRestRepository
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_PAGE
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.trending.TrendingMovies
import io.reactivex.rxjava3.core.Observable

class GetTrendingMoviesUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<TrendingMovies>>() {
    var page: Int = 0
    var apiKey = ""
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<TrendingMovies>> {
        var throwable: Throwable
        try {
            this.apiKey = withParam!![PARAM_API_KEY] as String
            this.page = withParam!![PARAM_PAGE] as Int
            return movieRestRepository.getTrendingMovies(this.apiKey, this.page)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getTrendingMovies(apiKey: String, page: Int): Observable<Optional<TrendingMovies>> {
        this.apiKey = apiKey
        this.page = page
        val params: HashMap<String, Any?> = hashMapOf()
        params[PARAM_API_KEY] = this.apiKey
        params[PARAM_PAGE] = this.page
        return observable(params)
    }
}

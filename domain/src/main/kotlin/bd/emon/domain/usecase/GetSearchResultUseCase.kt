package bd.emon.domain.usecase

import bd.emon.domain.MovieRestRepository
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_LANGUAGE
import bd.emon.domain.PARAM_PAGE
import bd.emon.domain.PARAM_SEARCH_QUERY
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.search.MovieSearch
import io.reactivex.rxjava3.core.Observable

class GetSearchResultUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<MovieSearch>>() {

    var params: HashMap<String, Any?>? = null
    lateinit var apiKey: String
    lateinit var language: String
    var page: Int = 0
    var includeAdult: Boolean = false
    lateinit var query: String

    fun getSearchResult(
        apiKey: String,
        language: String,
        page: Int,
        includeAdult: Boolean,
        query: String
    ): Observable<Optional<MovieSearch>> {
        this.apiKey = apiKey
        this.language = language
        this.page = page
        this.includeAdult = includeAdult
        this.query = query

        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = this.apiKey
        params[PARAM_LANGUAGE] = this.language
        params[PARAM_PAGE] = this.page
        params[PARAM_INCLUDE_ADULT] = this.includeAdult
        params[PARAM_SEARCH_QUERY] = this.query

        return observable(params)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MovieSearch>> {
        var throwable: Throwable
        try {
            return movieRestRepository.getSearchResult(
                apiKey = withParam!![PARAM_API_KEY] as String,
                language = withParam!![PARAM_LANGUAGE] as String,
                page = withParam!![PARAM_PAGE] as Int,
                includeAdult = withParam!![PARAM_INCLUDE_ADULT] as Boolean,
                query = withParam!![PARAM_SEARCH_QUERY] as String
            )
        } catch (t: Throwable) {
            throwable = t
        }
        return Observable.error(throwable)
    }
}

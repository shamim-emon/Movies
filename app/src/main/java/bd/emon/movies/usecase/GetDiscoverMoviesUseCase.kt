package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.common.Transformer
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.rest.MovieApis
import io.reactivex.rxjava3.core.Observable

class GetDiscoverMoviesUseCase(
    transformer: Transformer<Optional<DiscoverMovie>>,
    private val movieApis: MovieApis
) :UseCase<Optional<DiscoverMovie>>(transformer) {
    lateinit var apiKey: String
    lateinit var lang: String
    var sortBy: String?=null
    var includeAdult: Boolean?=null
    var page: Int?=null
    var vote_count_greater_than: Int?=null
    var genres: String?=null
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<DiscoverMovie>> {
        apiKey = withParam!![PARAM_API_KEY] as String
        lang = withParam[PARAM_LANGUAGE] as String
        sortBy = withParam[PARAM_SORT_BY] as String?
        includeAdult = withParam[PARAM_INCLUDE_ADULT] as Boolean?
        page = withParam[PARAM_PAGE] as Int?
        vote_count_greater_than = withParam[PARAM_VOTE_COUNT_GREATER_THAN] as Int?
        genres = withParam[PARAM_GENRES] as String?
        return Observable.just(Optional.empty())
    }
}
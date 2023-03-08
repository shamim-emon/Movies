package bd.emon.domain.usecase

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.domain.MovieCacheRepository
import bd.emon.domain.entity.Optional
import io.reactivex.rxjava3.core.Observable

class SaveCacheDiscoverMoviesFiltersUseCase(
    private val movieCacheRepository: MovieCacheRepository
) : UseCase<Optional<MutablePreferences>>() {

    var minVoteCount: Int = 0
    var includeAdultContent: Boolean = false
    lateinit var orderBy: String
    lateinit var releaseYearStr: String

    fun cacheFilterParams(
        minVoteCount: Int,
        includeAdultContent: Boolean,
        orderBy: String,
        releaseYearStr: String
    ): Observable<Optional<MutablePreferences>> {
        this.minVoteCount = minVoteCount
        this.includeAdultContent = includeAdultContent
        this.orderBy = orderBy
        this.releaseYearStr = releaseYearStr
        return observable()
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MutablePreferences>> {
        var throwable: Throwable
        try {
            return movieCacheRepository.saveDiscoverMovieFilters(
                this.minVoteCount,
                this.includeAdultContent,
                this.orderBy,
                this.releaseYearStr
            )
        } catch (t: Throwable) {
            throwable = t
        }
        return Observable.error(throwable)
    }
}

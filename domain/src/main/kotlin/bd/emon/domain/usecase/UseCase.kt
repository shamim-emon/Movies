package bd.emon.domain.usecase

import io.reactivex.rxjava3.core.Observable

abstract class UseCase<T : Any> {

    abstract fun createObservable(withParam: HashMap<String, Any?>? = null): Observable<T>

    fun observable(withParam: HashMap<String, Any?>? = null): Observable<T> {
        return createObservable(withParam)
    }
}

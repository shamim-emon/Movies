package bd.emon.movies.usecase

import bd.emon.movies.common.Transformer
import io.reactivex.rxjava3.core.Observable

abstract class UseCase<T>(private val transformer: Transformer<T>) {

    abstract fun createObservable(withParam: HashMap<String, Any>? = null): Observable<T>

    fun observable(withParam: HashMap<String, Any>? = null): Observable<T> {
        return createObservable(withParam).compose(transformer)
    }
}

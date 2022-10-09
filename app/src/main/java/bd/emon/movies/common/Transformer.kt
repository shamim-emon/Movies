package bd.emon.movies.common

import io.reactivex.rxjava3.core.ObservableTransformer

abstract class Transformer<T> : ObservableTransformer<T, T>

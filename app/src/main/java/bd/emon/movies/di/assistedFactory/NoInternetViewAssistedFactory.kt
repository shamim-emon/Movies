package bd.emon.movies.di.assistedFactory

import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.databinding.LayoutExceptionBinding
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoInternetViewAssistedFactory {
    @bd.emon.movies.di.qualifier.NoInternetView
    fun create(binding: LayoutExceptionBinding): NoInternetView
}

package bd.emon.movies.di.assistedFactory

import bd.emon.domain.view.NoInternetView
import bd.emon.movies.databinding.LayoutExceptionBinding
import bd.emon.movies.di.qualifier.NoInternet
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoInternetViewAssistedFactory {
    @NoInternet
    fun create(binding: LayoutExceptionBinding): NoInternetView
}

package bd.emon.movies.di.assistedFactory

import bd.emon.movies.common.view.NoContentView
import bd.emon.movies.databinding.LayoutExceptionBinding
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoContentViewAssistedFactory {
    @bd.emon.movies.di.qualifier.NoContentView
    fun create(binding: LayoutExceptionBinding): NoContentView
}

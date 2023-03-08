package bd.emon.movies.di.assistedFactory

import bd.emon.domain.view.NoContentView
import bd.emon.movies.databinding.LayoutExceptionBinding
import bd.emon.movies.di.qualifier.NoContent
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoContentViewAssistedFactory {
    @NoContent
    fun create(binding: LayoutExceptionBinding): NoContentView
}

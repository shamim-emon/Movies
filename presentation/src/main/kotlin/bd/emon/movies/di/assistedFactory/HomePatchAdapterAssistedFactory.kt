package bd.emon.movies.di.assistedFactory

import bd.emon.domain.entity.genre.Genre
import bd.emon.movies.home.HomeFragmentAdaptersCallBack
import bd.emon.movies.home.HomePatchesAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface HomePatchAdapterAssistedFactory {
    fun create(
        genres: List<Genre>,
        callBack: HomeFragmentAdaptersCallBack
    ): HomePatchesAdapter
}

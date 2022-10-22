package bd.emon.movies.di.assistedFactory

import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.home.DiscoverListAdapterCallBack
import bd.emon.movies.home.HomePatchesAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface HomePatchesAdapteFactory {
    fun create(genres: List<Genre>, callBack: DiscoverListAdapterCallBack): HomePatchesAdapter
}

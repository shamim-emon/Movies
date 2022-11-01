package bd.emon.movies.di.assistedFactory

import android.content.Context
import bd.emon.movies.home.FilterDialogAdaptersProvider
import bd.emon.movies.home.FilterDialogFacade
import bd.emon.movies.viewModels.HomeViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface FilterDialogFacadeAssistedFactory {
    fun create(
        apiParams: HashMap<String, Any?>,
        orderByAdapterProvider: FilterDialogAdaptersProvider<String>,
        yearAdapterProvider: FilterDialogAdaptersProvider<Int>,
        homeViewModel: HomeViewModel,
        context: Context
    ): FilterDialogFacade
}

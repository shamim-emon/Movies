package bd.emon.movies.di.assistedFactory

import android.app.Activity
import bd.emon.movies.common.menuItem.HomeMenuItemListener
import bd.emon.movies.viewModels.HomeViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface HomeMenuItemListenerAssistedFactory {
    fun create(homeViewModel: HomeViewModel, activity: Activity): HomeMenuItemListener
}

package bd.emon.movies.di.module

import bd.emon.movies.home.HomePatchAdapterViewHolderContainer
import bd.emon.movies.home.HomePatchAdapterViewHolderContainerImpl
import bd.emon.movies.home.HomePatchViewHolderHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
class HomeFragmentModule {
    @Provides
    fun provideHomePatchAdapterViewHolderContainer(): HomePatchAdapterViewHolderContainer =
        HomePatchAdapterViewHolderContainerImpl()

    @Provides
    fun provideHomePatchViewHolderHelper(): HomePatchViewHolderHelper = HomePatchViewHolderHelper()
}

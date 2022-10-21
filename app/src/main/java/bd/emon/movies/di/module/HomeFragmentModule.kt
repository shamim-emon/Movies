package bd.emon.movies.di.module

import bd.emon.movies.home.DiscoverListAdaptersContainer
import bd.emon.movies.home.DiscoverListAdaptersContainerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
class HomeFragmentModule {
    @Provides fun provideDiscoverListAdapterHelper() : DiscoverListAdaptersContainer = DiscoverListAdaptersContainerImpl()
}
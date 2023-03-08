package bd.emon.movies.di.entryPoint

import bd.emon.movies.home.FilterDialogBindingHelper
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@EntryPoint
@InstallIn(FragmentComponent::class)
interface FragmentEntryPoint {
    fun filterDialogBindingHelper(): FilterDialogBindingHelper
}

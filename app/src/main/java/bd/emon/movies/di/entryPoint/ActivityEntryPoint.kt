package bd.emon.movies.di.entryPoint

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ActivityEntryPoint {
    fun getMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder
}

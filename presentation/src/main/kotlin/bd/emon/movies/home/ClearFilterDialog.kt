package bd.emon.movies.home

import android.content.Context
import bd.emon.movies.R
import bd.emon.movies.viewModels.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClearFilterDialog(
    private val homeViewModel: HomeViewModel,
    private val context: Context,
    private val materialAlertDialogBuilder: MaterialAlertDialogBuilder
) : Dialog {
    override fun createAndDisplayDialog() {
        materialAlertDialogBuilder
            .setTitle(context.resources.getString(R.string.clear_filters))
            .setMessage(context.resources.getString(R.string.prompt_clear_filter))
            .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, which ->
                homeViewModel.clearFilterParams()
            }
            .setNegativeButton(context.resources.getString(R.string.no)) { dialog, which ->
            }
            .show()
    }
}

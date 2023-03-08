package bd.emon.domain.menuItem

import android.view.MenuItem
import bd.emon.movies.R
import bd.emon.movies.home.ClearFilterDialog
import bd.emon.movies.home.FilterDialogFacade

class HomeMenuItemListener(
    private val filterDialogFacade: FilterDialogFacade,
    private val clearFilterDialog: ClearFilterDialog
) :
    MenuItemListener() {
    override fun handleClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.filter -> showFilterDialog()
            R.id.clearFilter -> showClearFilterDialog()
        }
    }

    private fun showFilterDialog() {
        filterDialogFacade.createAndDisplayDialog()
    }

    private fun showClearFilterDialog() {
        clearFilterDialog.createAndDisplayDialog()
    }
}

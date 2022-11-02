package bd.emon.movies.common.menuItem

import android.view.MenuItem
import bd.emon.movies.R
import bd.emon.movies.home.FilterDialogFacade

class HomeMenuItemListener(private val filterDialogFacade: FilterDialogFacade) :
    MenuItemListener() {

    override fun handleClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.filter -> showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        filterDialogFacade.createAndDisplayDialog()
    }
}

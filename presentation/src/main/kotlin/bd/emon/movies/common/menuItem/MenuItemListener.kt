package bd.emon.domain.menuItem

import android.view.MenuItem

abstract class MenuItemListener {
    abstract fun handleClick(menuItem: MenuItem)
}

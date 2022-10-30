package bd.emon.movies.common.menuItem

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import bd.emon.movies.di.entryPoint.ActivityEntryPoint
import bd.emon.movies.di.entryPoint.AppEntryPoint
import dagger.hilt.android.EntryPointAccessors

abstract class MenuItemListener(activity: Activity) {

    private val context = activity as Context
    private val appContext = context.applicationContext
    private val appEntryPoint =
        EntryPointAccessors.fromApplication(appContext, AppEntryPoint::class.java)
    private val activityEntryPoint =
        EntryPointAccessors.fromActivity(activity, ActivityEntryPoint::class.java)

    protected val apiParams: HashMap<String, Any?> = appEntryPoint.getApiParams()
    protected val materialAlertDialogBuilder = activityEntryPoint.getMaterialAlertDialogBuilder()

    abstract fun handleClick(menuItem: MenuItem)
}

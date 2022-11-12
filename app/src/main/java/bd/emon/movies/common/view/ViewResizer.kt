package bd.emon.movies.common.view

import android.content.res.Resources
import android.view.View

class ViewResizer {

    private val width = Resources.getSystem().displayMetrics.widthPixels

    fun makeViewHalfScreenWidth(view: View, dpMargin: Int) {
        val marginPx = dpMargin * Resources.getSystem().displayMetrics.density
        val adjustedSize = ((width / 2) - marginPx).toInt()
        view.layoutParams.width = adjustedSize
    }
}

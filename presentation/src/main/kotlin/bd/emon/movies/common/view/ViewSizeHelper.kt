package bd.emon.domain.view

import android.content.res.Resources
import android.view.View

class ViewSizeHelper {

    private val width = Resources.getSystem().displayMetrics.widthPixels

    fun makeViewHalfScreenWidth(view: View, dpMargin: Int) {
        val marginPx = dpMargin * Resources.getSystem().displayMetrics.density
        val adjustedSize = ((width / 2) - marginPx).toInt()
        view.layoutParams.width = adjustedSize
    }
}

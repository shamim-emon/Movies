package bd.emon.movies.common

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import bd.emon.domain.entity.genre.Genre
import bd.emon.movies.databinding.LayoutLoaderBinding

fun List<Genre>.getGenreFromId(id: Int): String {
    var genre = ""
    this.forEach {
        if (it.id == id) {
            genre = it.name
            return@forEach
        }
    }
    return genre
}

fun LayoutLoaderBinding.hide() {
    this.root.layoutParams.height = 0
    this.progressBar.visibility = View.GONE
}

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

package bd.emon.movies.common

import android.content.res.Resources
import android.util.TypedValue
import bd.emon.movies.entity.genre.Genre

fun String.toApiParam() = this.replace("param:", "")
fun String.toLowerCaseNoSpace() = this.lowercase().trim().replace(" ", "_")
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

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

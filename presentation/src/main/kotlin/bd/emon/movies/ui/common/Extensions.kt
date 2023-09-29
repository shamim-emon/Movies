package bd.emon.movies.ui.common

import kotlin.math.ln

fun Long.formatHumanReadable(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / Math.log(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        this / Math.pow(1000.0, exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}
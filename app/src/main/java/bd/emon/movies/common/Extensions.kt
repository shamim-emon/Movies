package bd.emon.movies.common

fun String.toApiParam() = this.replace("param:", "")
fun String.toLowerCaseNoSpace() = this.lowercase().trim().replace(" ", "_")

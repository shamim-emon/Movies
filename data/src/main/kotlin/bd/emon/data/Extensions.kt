package bd.emon.data

fun String.toApiParam() = this.replace("param:", "")
fun String.toLowerCaseNoSpace() = this.lowercase().trim().replace(" ", "_")

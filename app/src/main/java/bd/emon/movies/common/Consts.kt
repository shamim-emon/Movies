package bd.emon.movies.common

const val IMAGE_BASE_URL ="https://image.tmdb.org/t/p/original"

const val NO_DATA_ERR = "Missing Data"
const val NETWORK_ERROR_DEFAULT = "No Internet"

const val PARAM_API_KEY = "param:api_key"
const val PARAM_LANGUAGE = "param:language"
const val PARAM_SORT_BY = "param:sort_by"
const val PARAM_INCLUDE_ADULT = "param:include_adult"
const val PARAM_PAGE = "param:page"
const val PARAM_VOTE_COUNT_GREATER_THAN = "param:vote_count.gte"
const val PARAM_GENRES = "param:with_genres"


fun String.toApiParam() = this.replace("param:", "")


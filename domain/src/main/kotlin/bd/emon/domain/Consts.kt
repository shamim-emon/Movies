package bd.emon.domain

const val DATA_STORE_NAME = "MovieCache"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
const val DEFAULT_MINIMUM_VOTE_COUNT = 3000

const val NO_DATA_ERR = "Missing Data"
const val NO_FAV = "You Don't have any favourite movie"
const val NETWORK_ERROR_DEFAULT = "No Internet"
const val SAVE_TO_PREF_ERROR_DEFAULT = "unable to cache data"
const val DB_TRANSACTION_ERROR = "Unable to perform the transation"

const val PARAM_API_KEY = "param:api_key"
const val PARAM_MOVIE_ID = "param:movie_id"
const val PARAM_LANGUAGE = "param:language"
const val PARAM_SORT_BY = "param:sort_by"
const val PARAM_INCLUDE_ADULT = "param:include_adult"
const val PARAM_PAGE = "param:page"
const val PARAM_VOTE_COUNT_GREATER_THAN = "param:vote_count.gte"
const val PARAM_GENRES = "param:with_genres"
const val PARAM_RELEASE_YEAR = "param:primary_release_year"
const val PARAM_SEARCH_QUERY = "param:query"
const val PARAM_MOVIE_ENTITY = "param:movieEntity"

const val DESC = "desc"
const val CURRENT_YEAR = "current_year"
const val POPULARITY = "popularity"
const val RELEASE_YEAR = "release_year"
const val REVENUE = "revenue"
const val MOVIE_TITLE = "original_title"
const val VOTE_COUNT = "vote_count"
const val DEFAULT_ORDER_BY = "popularity.desc"

const val DEFAULT_VIEW_RESIZE_MARGIN = 24
const val INVALID_VIEW_HOLDER = "Invalid View type"
const val INVALID_API_CALL_TYPE = "Invalid api call type"
const val DEBOUNCE_DEAULT_DURATION = 600L

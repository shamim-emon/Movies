package bd.emon.movies.home

interface DiscoverListAdapterCallBack {
    fun loadDiscoverItemByGenreId(genreId: Int)
    fun showLoader(genreId: Int)
}

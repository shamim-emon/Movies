package bd.emon.movies.home

interface HomeFragmentAdaptersCallBack {
    fun loadDiscoverItemByGenreId(genreId: Int)
    fun goToViewAll(genreId: Int, genre: String)
}

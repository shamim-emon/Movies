package bd.emon.movies.home

interface HomeFragmentAdaptersCallBack {
    fun loadDiscoverItemByGenreId(genreId: Int)
    fun seeMore(genreId: Int, genre: String)
}

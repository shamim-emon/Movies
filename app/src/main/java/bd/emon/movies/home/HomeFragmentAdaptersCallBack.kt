package bd.emon.movies.home

interface HomeFragmentAdaptersCallBack {
    fun loadDiscoverItemByGenreId(genreId: Int)
    fun goToMovieEntityList(genreId: Int, genre: String)
}

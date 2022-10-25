package bd.emon.movies.home

interface HomePatchAdapterViewHolderHelper {

    fun handleViewHolderSuccess(
        viewHolder: HomePatchesAdapter.ViewHolder?,
        movies: MutableList<bd.emon.movies.entity.discover.Result>
    )

    fun handleViewHolderError(
        viewHolder: HomePatchesAdapter.ViewHolder?
    )

    fun showLoading(viewHolder: HomePatchesAdapter.ViewHolder?)

    fun hideLoading(viewHolder: HomePatchesAdapter.ViewHolder?)
}

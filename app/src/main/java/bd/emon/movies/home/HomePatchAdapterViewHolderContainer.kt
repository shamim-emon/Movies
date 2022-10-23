package bd.emon.movies.home

interface HomePatchAdapterViewHolderContainer {
    fun addViewHolder(key: Int, viewHolder: HomePatchesAdapter.ViewHolder)
    fun getViewHolder(key: Int): HomePatchesAdapter.ViewHolder?
}

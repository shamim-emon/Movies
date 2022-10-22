package bd.emon.movies.home

interface DiscoverListAdaptersContainer {
    fun addAdapterToContainer(key: Int, adapter: DiscoverListAdapter)
    fun getAdapterFromContainer(key: Int): DiscoverListAdapter?
}

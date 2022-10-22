package bd.emon.movies.home

class DiscoverListAdaptersContainerImpl : DiscoverListAdaptersContainer {
    private val adapterMap: HashMap<Int, DiscoverListAdapter> = hashMapOf()

    override fun addAdapterToContainer(key: Int, adapter: DiscoverListAdapter) {
        if (!containsAdapter(key)) {
            adapterMap[key] = adapter
        }
    }

    override fun getAdapterFromContainer(key: Int) = adapterMap[key]

    private fun containsAdapter(key: Int) = adapterMap.containsKey(key)
}

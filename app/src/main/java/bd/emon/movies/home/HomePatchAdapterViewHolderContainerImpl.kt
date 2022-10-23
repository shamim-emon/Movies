package bd.emon.movies.home

class HomePatchAdapterViewHolderContainerImpl : HomePatchAdapterViewHolderContainer {

    private val map: HashMap<Int, HomePatchesAdapter.ViewHolder> = hashMapOf()

    override fun addViewHolder(key: Int, viewHolder: HomePatchesAdapter.ViewHolder) {
        viewHolder.tag?.let {
            map.remove(it)
        }
        viewHolder.tag = key
        map[key] = viewHolder
    }

    override fun getViewHolder(key: Int) = map[key]
}

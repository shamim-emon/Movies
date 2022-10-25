package bd.emon.movies.home

import bd.emon.movies.entity.discover.Result

class DiscoverItemDataContainer {
    private val discoverItemListMap: HashMap<Int, MutableList<Result>> =
        hashMapOf()

    fun addDiscoverListToMap(key: Int, list: MutableList<Result>) {
        discoverItemListMap[key] = list
    }

    fun getDistcoverListFromMap(key: Int): MutableList<Result>? {
        return discoverItemListMap[key]
    }
}

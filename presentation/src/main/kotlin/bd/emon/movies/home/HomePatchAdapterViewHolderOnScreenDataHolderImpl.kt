package bd.emon.movies.home

import bd.emon.domain.entity.discover.Result

class HomePatchAdapterViewHolderOnScreenDataHolderImpl : HomePatchAdapterViewHolderOnScreenDataHolder {
    private var discoverItemListMap: HashMap<Int, MutableList<Result>> =
        hashMapOf()

    override fun clearAnyOnHoldData() {
        discoverItemListMap = hashMapOf()
    }

    override fun addDiscoverListToMap(key: Int, list: MutableList<Result>) {
        discoverItemListMap[key] = list
    }

    override fun getDistcoverListFromMap(key: Int): MutableList<Result>? {
        return discoverItemListMap[key]
    }
}

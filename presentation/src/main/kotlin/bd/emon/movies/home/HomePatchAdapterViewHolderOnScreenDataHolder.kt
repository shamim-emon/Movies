package bd.emon.movies.home

import bd.emon.domain.entity.discover.Result

interface HomePatchAdapterViewHolderOnScreenDataHolder {
    fun clearAnyOnHoldData()
    fun addDiscoverListToMap(key: Int, list: MutableList<Result>)
    fun getDistcoverListFromMap(key: Int): MutableList<Result>?
}

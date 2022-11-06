package bd.emon.movies.home

import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding

interface FilterDialogBindingHelper {
    fun setBinding(binding: LayoutDiscoverMovieFilterBinding)
    fun setMinVoteUpperLimit()
    fun setMinVoteValueChangeListener()
    fun setData(map: HashMap<String, Any?>)
    fun updateApiParam(map: HashMap<String, Any?>): HashMap<String, Any?>
}

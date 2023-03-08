package bd.emon.movies.home

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.domain.entity.discover.Result
import bd.emon.domain.view.NoContentView
import bd.emon.domain.view.NoInternetView
import bd.emon.domain.view.ViewSizeHelper
import bd.emon.movies.common.MovieDetailsNavigator
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class HomePatchAdapterViewHolderFacade @Inject constructor(
    private val container: HomePatchAdapterViewHolderContainer,
    private val onScreenHolder: HomePatchAdapterViewHolderOnScreenDataHolder,
    private val mapper: DiscoverMovieMapper,
    private val viewResizer: ViewSizeHelper
) {
    private var movieDetailsNavigator: MovieDetailsNavigator? = null

    fun attachMovieDetailsNavigator(movieDetailsNavigator: MovieDetailsNavigator) {
        this.movieDetailsNavigator = movieDetailsNavigator
    }

    fun handleViewHolderSuccess(
        viewHolder: HomePatchesAdapter.ViewHolder?,
        movies: MutableList<Result>
    ) {
        viewHolder?.let { vh ->
            val context = viewHolder.binding.exceptionView.root.context
            val noContentView = NoContentView(viewHolder.binding.exceptionView, context)
            if (movies.size == 0) {
                noContentView.layoutAndshowExceptionView()
                vh.binding.seeAll.visibility = GONE
            } else {
                noContentView.hideExceptionView()
                vh.binding.seeAll.visibility = VISIBLE
                vh.binding.list.adapter =
                    MovieListAdapter(movies = mapper.mapFrom(movies), viewResizer = viewResizer, movieDetailsNavigator = this.movieDetailsNavigator)
                vh.binding.list.layoutManager =
                    LinearLayoutManager(
                        vh.binding.list.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
            }
        }
    }

    fun handleViewHolderError(
        viewHolder: HomePatchesAdapter.ViewHolder?
    ) {
        viewHolder?.let { vh ->
            val context = viewHolder.binding.exceptionView.root.context
            val noInternetView = NoInternetView(viewHolder.binding.exceptionView, context)
            noInternetView.layoutAndshowExceptionView()
        }
    }

    fun showLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = View.GONE
    }

    fun addViewHolder(key: Int, viewHolder: HomePatchesAdapter.ViewHolder) {
        container.addViewHolder(key, viewHolder)
    }

    fun getViewHolder(key: Int): HomePatchesAdapter.ViewHolder? {
        return container.getViewHolder(key)
    }

    fun clearAnyOnHoldData() {
        onScreenHolder.clearAnyOnHoldData()
    }

    fun addDiscoverListToMap(key: Int, list: MutableList<Result>) {
        onScreenHolder.addDiscoverListToMap(key, list)
    }

    fun getDistcoverListFromMap(key: Int): MutableList<Result>? {
        return onScreenHolder.getDistcoverListFromMap(key)
    }

    fun inflateHomePatchViewHolder(
        genreId: Int,
        list: MutableList<Result>
    ) {
        val viewHolder = getViewHolder(genreId)
        hideLoading(viewHolder)
        addDiscoverListToMap(genreId, list)
        handleViewHolderSuccess(viewHolder, list)
    }
}

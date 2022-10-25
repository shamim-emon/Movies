package bd.emon.movies.home

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.common.view.NoContentView
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.throwable.DiscverMovieThrowable

class HomePatchViewHolderHelper {
    fun handleViewHolderSuccess(
        viewHolder: HomePatchesAdapter.ViewHolder?,
        movies: MutableList<bd.emon.movies.entity.discover.Result>
    ) {
        viewHolder?.let { vh ->
            vh.hasData = true
            val context = viewHolder.binding.exceptionView.root.context
            val noContentView = NoContentView(viewHolder.binding.exceptionView, context)
            if (movies.size == 0) {
                noContentView.layoutAndshowExceptionView()
            } else {
                noContentView.hideExceptionView()
                vh.binding.list.adapter = DiscoverListAdapter(movies)
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
        viewHolder: HomePatchesAdapter.ViewHolder?,
        throwable: DiscverMovieThrowable
    ) {
        viewHolder?.let { vh ->
            val context = viewHolder.binding.exceptionView.root.context
            if (throwable.errorMessage == NO_DATA_ERR) {
                val noContentView = NoContentView(viewHolder.binding.exceptionView, context)
                noContentView.layoutAndshowExceptionView()
            } else if (throwable.errorMessage != NO_DATA_ERR && !vh.hasData) {
                val noInternetView = NoInternetView(viewHolder.binding.exceptionView, context)
                noInternetView.layoutAndshowExceptionView()
            }
        }
    }

    fun showLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = VISIBLE
    }

    fun hideLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = GONE
    }
}

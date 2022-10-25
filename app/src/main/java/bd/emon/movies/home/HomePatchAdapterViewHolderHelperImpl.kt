package bd.emon.movies.home

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.common.view.NoContentView
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.entity.discover.Result

class HomePatchAdapterViewHolderHelperImpl : HomePatchAdapterViewHolderHelper {
    override fun handleViewHolderSuccess(
        viewHolder: HomePatchesAdapter.ViewHolder?,
        movies: MutableList<Result>
    ) {
        viewHolder?.let { vh ->
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

    override fun handleViewHolderError(
        viewHolder: HomePatchesAdapter.ViewHolder?
    ) {
        viewHolder?.let { vh ->
            val context = viewHolder.binding.exceptionView.root.context
            val noInternetView = NoInternetView(viewHolder.binding.exceptionView, context)
            noInternetView.layoutAndshowExceptionView()
        }
    }

    override fun showLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = VISIBLE
    }

    override fun hideLoading(viewHolder: HomePatchesAdapter.ViewHolder?) {
        viewHolder?.binding?.progressBar?.visibility = GONE
    }
}

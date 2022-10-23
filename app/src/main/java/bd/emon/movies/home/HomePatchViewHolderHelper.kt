package bd.emon.movies.home

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager

class HomePatchViewHolderHelper {
    fun handleViewHolder(
        viewHolder: HomePatchesAdapter.ViewHolder?,
        movies: MutableList<bd.emon.movies.entity.discover.Result>
    ) {
        viewHolder?.let { vh ->
            vh.binding.progressBar.visibility = View.GONE
            if (movies.size == 0) {
                vh.binding.noDataView.root.visibility = VISIBLE
            } else {
                vh.binding.noDataView.root.visibility = GONE
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
}

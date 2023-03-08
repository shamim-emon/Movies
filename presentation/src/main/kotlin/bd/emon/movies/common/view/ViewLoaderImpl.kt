package bd.emon.domain.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ViewLoaderImpl(private val swipeRefresh: SwipeRefreshLayout) : ViewLoader {
    init {
        swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    override fun showLoader() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoader() {
        swipeRefresh.isRefreshing = false
    }
}

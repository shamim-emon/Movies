package bd.emon.movies.ui.trending

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import bd.emon.data.dataMapper.TrendingMovieMapper
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.trending.TrendingMovies
import bd.emon.movies.viewModels.TrendingViewModel
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun TrendingRoute(
    trendingMovieMapper: TrendingMovieMapper
) {
    val viewModel: TrendingViewModel = hiltViewModel()
    val loadState by viewModel.loadingState.observeAsState(initial = true)
    val pullRefreshState: SwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = loadState)
    val tendingMovies by viewModel.trendingMovies.observeAsState(
        initial = TrendingMovies(
            page = 0,
            total_pages = 0,
            results = mutableListOf(),
            total_results = 0
        )
    )

    val movieEntities: MutableList<MovieEntity> = remember { mutableStateListOf() }
    val apiKey = viewModel.apiParams[PARAM_API_KEY] as String
    val page by remember { mutableIntStateOf(0) }
    val loadData: () -> Unit = {
        viewModel.loadTrendingMovies(apiKey = apiKey, page = page)
    }

    val addMovieEntities: () -> Unit = {
        movieEntities.addAll(trendingMovieMapper.mapFrom(tendingMovies.results))
    }
    TrendingScreen(loadState = loadState, pullRefreshState = pullRefreshState)
}
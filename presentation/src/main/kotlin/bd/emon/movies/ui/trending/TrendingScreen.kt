package bd.emon.movies.ui.trending

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import bd.emon.movies.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    loadState: Boolean,
    pullRefreshState: SwipeRefreshState,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.trending_tab),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = modifier,
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = null
            )
        },
    ) { paddingValues ->
        SwipeRefresh(state = pullRefreshState, onRefresh = {}){
            val contentModifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        }
    }
}
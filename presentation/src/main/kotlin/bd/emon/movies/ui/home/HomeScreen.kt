package bd.emon.movies.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.genre.Genres
import bd.emon.movies.R
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
import bd.emon.movies.ui.common.WaitView
import bd.emon.movies.ui.theme.MovieTheme
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    loadState: Boolean,
    genres: Genres?,
    pullRefreshState: SwipeRefreshState,
    loadGenres: () -> Unit,
    loadDiscoverMoviesByGenreId: (String) -> Unit,
    movieMap: SnapshotStateMap<Int, List<MovieEntity>>?
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_tab),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = modifier,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterAltOff, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = null)
                    }
                },
                navigationIcon = {},
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = null
            )
        }
    ) { paddingValues ->

        SwipeRefresh(state = pullRefreshState, onRefresh = { loadGenres() }) {

            val contentModifier = modifier
                .fillMaxSize()
                .padding(paddingValues)

            when (loadState) {
                true -> {
                    WaitView(
                        modifier = contentModifier
                    )
                }

                else -> {
                    ContentView(
                        modifier = contentModifier,
                        genres = genres,
                        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
                        movieMap = movieMap
                    )
                }
            }
        }
        loadGenres()
    }
}

@Preview("HomeScreen(Light)", uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Preview("HomeScreen(Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun HomePreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // HomeScreen(loadState = false, loadGenres = {})
        }
    }
}

@Composable
fun ContentView(
    modifier: Modifier = Modifier,
    genres: Genres?,
    loadDiscoverMoviesByGenreId: (String) -> Unit,
    movieMap: SnapshotStateMap<Int, List<MovieEntity>>?
) {

    genres?.let {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(
                items = genres.genres,
                key = { item -> item.id }
            ) { genre ->
                Text(
                    text = genre.name,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (movieMap?.containsKey(genre.id) == false) {
                    loadDiscoverMoviesByGenreId("${genre.id}")
                } else {
                    val movies = movieMap!![genre.id]
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(
                            items = movies!!,
                            key = { item -> item.id }
                        ) {
                            MovieThumb(movieEntity = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieThumb(
    modifier: Modifier = Modifier,
    movieEntity: MovieEntity = MovieApiDummyDataProvider.movieEntity
) {
    Column(
        modifier = modifier
            .width(width = 120.dp)
            .height(height = 232.dp),
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .height(height = 180.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Image(
                painter = rememberAsyncImagePainter(movieEntity.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = movieEntity.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
}

@Preview(
    "MovieThumbPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    "MovieThumbPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun MovieThumbPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MovieThumb()
        }
    }
}

package bd.emon.movies.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import bd.emon.movies.ui.common.ErrorImage
import bd.emon.movies.ui.common.NoInternetView
import bd.emon.movies.ui.common.PlaceHolderImage
import bd.emon.movies.ui.common.WaitView
import bd.emon.movies.ui.common.defaultThumbSize
import bd.emon.movies.ui.theme.MovieTheme
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    loadState: Boolean,
    genreErrorState: Throwable?,
    genres: Genres?,
    pullRefreshState: SwipeRefreshState,
    loadGenres: () -> Unit,
    loadDiscoverMoviesByGenreId: (String) -> Unit,
    movieMap: MutableMap<Int, List<MovieEntity>>
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarErrorMessage = stringResource(id = R.string.no_internet_secondary_text)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
            when {
                genreErrorState != null && movieMap.isNotEmpty() -> {
                    HomeContent(
                        modifier = contentModifier,
                        genres = genres,
                        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
                        movieMap = movieMap
                    )

                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(message = snackBarErrorMessage)
                    }
                }

                genreErrorState != null && movieMap.isEmpty() -> {
                    NoInternetView(
                        modifier = contentModifier
                            .verticalScroll(
                                rememberScrollState()
                            )
                    )
                }

                loadState -> {
                    WaitView(
                        modifier = contentModifier
                    )
                }

                !loadState -> {
                    HomeContent(
                        modifier = contentModifier,
                        genres = genres,
                        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
                        movieMap = movieMap
                    )
                }
            }
        }
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
fun HomeContent(
    modifier: Modifier = Modifier,
    genres: Genres?,
    loadDiscoverMoviesByGenreId: (String) -> Unit,
    movieMap: MutableMap<Int, List<MovieEntity>>?
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = genre.name,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1.0f),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                when (movieMap?.containsKey(genre.id)) {
                    true -> {
                        val movies = movieMap[genre.id]
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(
                                items = movies!!,
                                key = { item -> item.id }
                            ) {
                                MovieThumb(
                                    movieEntity = it
                                )
                            }
                        }
                    }

                    else -> {
                        loadDiscoverMoviesByGenreId("${genre.id}")
                        WaitView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(232.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieThumb(
    modifier: Modifier = Modifier,
    movieEntity: MovieEntity
) {
    Column(
        modifier = modifier
            .width(width = 120.dp)
            .height(height = 232.dp),
    ) {
        Card(
            modifier = Modifier.defaultThumbSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            SubcomposeAsyncImage(
                model = movieEntity.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    PlaceHolderImage()
                },
                error = {
                    ErrorImage()
                }
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
    name = "MovieThumbPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 120,
    heightDp = 232
)
@Preview(
    name = "MovieThumbPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 120,
    heightDp = 232
)
@Composable
fun MovieThumbPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MovieThumb(movieEntity = MovieApiDummyDataProvider.movieEntity)
        }
    }
}

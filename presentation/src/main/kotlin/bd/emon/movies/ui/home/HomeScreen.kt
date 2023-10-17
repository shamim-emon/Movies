package bd.emon.movies.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import bd.emon.movies.ui.common.MovieThumb
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import bd.emon.domain.DESC
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.domain.SAVE_TO_PREF_ERROR_DEFAULT
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.movies.R
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
import bd.emon.movies.ui.common.ListDivider
import bd.emon.movies.ui.common.NoInternetView
import bd.emon.movies.ui.common.WaitView
import bd.emon.movies.ui.common.formatHumanReadable
import bd.emon.movies.ui.theme.MovieTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    loadState: Boolean,
    genreErrorState: Throwable?,
    saveFilterErrorState: Throwable?,
    genres: Genres,
    pullRefreshState: SwipeRefreshState,
    loadOrReloadHomeScree: (() -> Unit)? = null,
    loadDiscoverMoviesByGenreId: ((String) -> Unit)? = null,
    clearFilters: (() -> Unit)? = null,
    clearFilterErrorState: (() -> Unit)? = null,
    updateFilters: ((Int, Boolean, String, String) -> Unit)? = null,
    movies: DiscoverMovies,
    movieReleaseYears: Array<String>,
    filters: HashMap<String, Any?>,
    movieMap: MutableMap<Int, List<MovieEntity>>,
    addToMovieMap: ((DiscoverMovies) -> Unit)? = null
) {

    if (!movieMap.containsKey(movies.grp_genre_id)) {
        addToMovieMap?.invoke(movies)
    }
    val snackbarHostState = remember { SnackbarHostState() }
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
                    var showClearFilter by remember { mutableStateOf(false) }
                    var showAddFilter by remember { mutableStateOf(false) }
                    IconButton(onClick = { showClearFilter = true }) {
                        Icon(imageVector = Icons.Filled.FilterAltOff, contentDescription = null)
                    }
                    IconButton(onClick = { showAddFilter = true }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = null)
                    }
                    when (showClearFilter) {
                        true -> ClearFilter(
                            onDismissRequest = { showClearFilter = false },
                            onConfirmation = {
                                showClearFilter = false
                                clearFilters?.invoke()
                            }
                        )

                        else -> {}
                    }

                    when (showAddFilter) {
                        true -> {
                            AddFilters(
                                dismissRequest = { showAddFilter = false },
                                movieReleaseYears = movieReleaseYears,
                                filters = filters,
                                updateFilters = updateFilters
                            )
                        }

                        else -> {}
                    }
                },
                navigationIcon = {},
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = null
            )
        }
    ) { paddingValues ->

        SwipeRefresh(state = pullRefreshState, onRefresh = { loadOrReloadHomeScree?.invoke() }) {

            val contentModifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
            when {
                genreErrorState != null -> {
                    NoInternetView(
                        modifier = contentModifier
                            .verticalScroll(
                                rememberScrollState()
                            )
                    )
                }

                loadState -> {
                    WaitView(modifier = contentModifier)
                }

                !loadState -> {
                    HomeContent(
                        modifier = contentModifier,
                        genres = genres,
                        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
                        movieMap = movieMap
                    )
                }

                saveFilterErrorState != null -> {
                    val snackBarErrorMessage = SAVE_TO_PREF_ERROR_DEFAULT
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(message = snackBarErrorMessage)
                    }
                    clearFilterErrorState?.invoke()
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
            HomeScreen(
                loadState = false,
                genreErrorState = null,
                saveFilterErrorState = null,
                genres = MovieApiDummyDataProvider.genreList,
                pullRefreshState = rememberSwipeRefreshState(isRefreshing = false),
                movies = MovieApiDummyDataProvider.disocoverMovies,
                movieReleaseYears = arrayOf("2023"),
                filters = hashMapOf(),
                movieMap = mutableMapOf(
                    80 to MovieApiDummyDataProvider.movieEntities,
                    28 to MovieApiDummyDataProvider.movieEntities,
                )
            )
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    genres: Genres,
    loadDiscoverMoviesByGenreId: ((String) -> Unit)? = null,
    movieMap: MutableMap<Int, List<MovieEntity>>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        items(
            items = genres.genres
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
            when (movieMap.containsKey(genre.id)) {
                true -> {
                    val movies = movieMap[genre.id] ?: listOf()
                    when (movies.isNotEmpty()) {
                        true -> {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                items(
                                    items = movies,
                                    key = { item -> item.id }
                                ) {
                                    MovieThumb(
                                        modifier = Modifier
                                            .width(width = 120.dp)
                                            .height(height = 232.dp),
                                        movieEntity = it
                                    )
                                }
                            }
                        }

                        else -> {
                            NoContentView(modifier = Modifier.fillMaxSize())
                        }
                    }
                }

                else -> {
                    loadDiscoverMoviesByGenreId?.invoke("${genre.id}")
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

@Preview(
    name = "AddFiltersPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    name = "AddFiltersPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun AddFiltersPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddFilters(
                dismissRequest = { },
                movieReleaseYears = arrayOf("2023", "2022", "2021", "2020"),
                filters = mutableMapOf()
            )
        }
    }
}

@Composable
fun AddFilters(
    modifier: Modifier = Modifier,
    dismissRequest: () -> Unit,
    movieReleaseYears: Array<String>,
    filters: MutableMap<String, Any?>,
    updateFilters: ((Int, Boolean, String, String) -> Unit)? = null

) {
    Dialog(onDismissRequest = dismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            var sliderPosition by remember {
                filters[PARAM_VOTE_COUNT_GREATER_THAN]?.let {
                    mutableFloatStateOf((it as Int).toFloat())
                } ?: run {
                    mutableFloatStateOf(0.0f)
                }
            }
            val sortingCriteria: Array<String> =
                stringArrayResource(id = R.array.movie_sorting_criteria)
            var selectedSortCriterion by remember {
                filters[PARAM_SORT_BY]?.let {
                    mutableStateOf(it.toString().replace(".$DESC", ""))
                } ?: run {
                    mutableStateOf(sortingCriteria[0])
                }
            }

            var selectedMovieReleaseYear by remember {
                filters[PARAM_RELEASE_YEAR]?.let {
                    mutableStateOf(it as String)
                } ?: run {
                    mutableStateOf(movieReleaseYears[0])
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Top)
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.FilterList, contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.filters),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.minimum_vote),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = sliderPosition.toLong().formatHumanReadable(),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = MaterialTheme.colorScheme.inversePrimary,
                    ),
                    valueRange = 0.0f..9999999.0f
                )
                ListDivider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.order_by),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    FilterDropDownMenu(
                        items = sortingCriteria,
                        selectedItem = selectedSortCriterion,
                        itemClick = { selectedSortCriterion = it }
                    )
                }
                ListDivider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.release_year),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    FilterDropDownMenu(
                        items = movieReleaseYears,
                        selectedItem = selectedMovieReleaseYear,
                        itemClick = { selectedMovieReleaseYear = it }
                    )
                }
                ListDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { dismissRequest() }
                    ) {
                        Text(
                            stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            dismissRequest()
                            updateFilters?.invoke(
                                sliderPosition.toInt(),
                                false,
                                selectedSortCriterion,
                                selectedMovieReleaseYear
                            )
                        }
                    ) {
                        Text(
                            stringResource(id = R.string.save),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropDownMenu(
    items: Array<String>,
    selectedItem: String,
    itemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var dropDownIcon = when (expanded) {
        false -> Icons.Filled.KeyboardArrowDown
        else -> Icons.Filled.KeyboardArrowUp
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { expanded = !expanded }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedItem,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = dropDownIcon,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        expanded = false
                        itemClick(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ClearFilter(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.FilterAltOff, contentDescription = null)
        },
        title = {
            Text(
                text = stringResource(id = R.string.clear_filters),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.prompt_clear_filter),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    stringResource(id = R.string.yes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    stringResource(id = R.string.no),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}

@Preview(
    name = "ClearFilterDialogPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    name = "ClearFilterDialogPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun ClearFilterPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ClearFilter(
                onConfirmation = {},
                onDismissRequest = {}
            )
        }
    }
}

@Preview(
    name = "NoContentViewPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    name = "NoContentViewPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun NoContentViewPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NoContentView(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun NoContentView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
            Icon(
                imageVector = Icons.Filled.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(124.dp),
            )
        }
        Text(
            text = stringResource(id = R.string.no_result_primary_text),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(id = R.string.no_result_secondary_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}
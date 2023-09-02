package bd.emon.movies.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bd.emon.movies.R
import bd.emon.movies.ui.theme.MovieTheme
import bd.emon.movies.ui.theme.teal200
import bd.emon.movies.ui.theme.teal700
import bd.emon.movies.ui.theme.teal800

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_tab)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterAltOff, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(10) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = "Section Title",
                            modifier = Modifier
                                .padding(start = 16.dp),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.teal200
                        )
                        Text(text = "See All",modifier = Modifier
                            .padding(end = 16.dp)
                            .align(Alignment.TopEnd),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.teal800
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ){
                        items(10){
                            MovieThumb()
                        }
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    MovieTheme {
        HomeScreen()
    }
}


@Composable
fun MovieThumb() {
    Column(Modifier.width(240.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
            backgroundColor = Color.LightGray,
            elevation = 8.dp
        )
        {
          Image(
              painter = painterResource(id = R.drawable.place_holder_w240_h360),
              contentDescription = "",
              contentScale = ContentScale.Crop
          )
        }
        Text(
            text = "Movie Title",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.teal700
        )
    }
}
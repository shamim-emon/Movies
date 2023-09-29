package bd.emon.movies.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bd.emon.movies.R
import bd.emon.movies.ui.theme.MovieTheme

@Preview(
    name = "PlaceHolderImage(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 120,
    heightDp = 180
)
@Preview(
    name = "PlaceHolderImage(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 120,
    heightDp = 180
)
@Composable
fun PlaceHolderImagePreview() {
    PlaceHolderImage(
        modifier = Modifier.defaultThumbSize()
    )
}

@Composable
fun PlaceHolderImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.place_holder_w240_h360),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Preview(
    name = "ErrorImage(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 120,
    heightDp = 180
)
@Preview(
    name = "ErrorImage(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 120,
    heightDp = 180
)
@Composable
fun ErrorImagePreview() {
    ErrorImage(
        modifier = Modifier.defaultThumbSize()
    )
}

@Composable
fun ErrorImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.error_w240_h360),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun WaitView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoInternetView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(124.dp),
            )
        }
        Text(
            text = stringResource(id = R.string.no_internet_primary_text),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(id = R.string.no_internet_secondary_text),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(
    name = "WaitViewPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    name = "WaitViewPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun WaitViewPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WaitView(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(
    name = "WaitViewPreview(Light)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    name = "WaitViewPreview(Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun NoInternetViewPreview() {
    MovieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NoInternetView(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun ListDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}
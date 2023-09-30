package bd.emon.movies

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.ActivityMainBinding
import bd.emon.movies.home.MovieReleaseYearsProvider
import bd.emon.movies.ui.MovieNavHost
import bd.emon.movies.ui.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var discoverMovieMapper: DiscoverMovieMapper

    @Inject
    lateinit var movieReleaseYearsProvider: MovieReleaseYearsProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieNavHost(
                        discoverMovieMapper = discoverMovieMapper,
                        movieReleaseYearsProvider = movieReleaseYearsProvider
                    )
                }
            }
        }
    }
}
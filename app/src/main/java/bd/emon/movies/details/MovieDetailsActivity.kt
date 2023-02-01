package bd.emon.movies.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.R
import bd.emon.movies.databinding.ActivityMovieDetailsBinding
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import bd.emon.movies.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    private val args: MovieDetailsActivityArgs by navArgs()
    private lateinit var movieId: String

    @Inject
    @ApiKey
    lateinit var apiKey: String

    @Inject
    @AppLanguage
    lateinit var language: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        movieId = args.movieId
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        viewModel.getMovieDetails(apiKey = apiKey, language = language, movieId = movieId)
        viewModel.getMovieVideos(apiKey = apiKey, movieId = movieId)

        viewModel.movieDetails.observe(this) {
            binding.details = it
        }

        viewModel.movieVideos.observe(this) {
            binding.trailerList.adapter = TrailersAdapter(it.results)
            binding.trailerList.layoutManager = LinearLayoutManager(this)
            binding.trailerList.isNestedScrollingEnabled = false
        }

//        binding.detailsAppbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
//            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
//                when (state) {
//                    State.COLLAPSED -> {
//                        binding.thumbnail.visibility = INVISIBLE
//                        binding.detailsBackdrop.visibility = INVISIBLE
//                    }
//                    else -> {
//                        binding.thumbnail.visibility = VISIBLE
//                        binding.detailsBackdrop.visibility = VISIBLE
//                    }
//                }
//            }
//        })
    }
}

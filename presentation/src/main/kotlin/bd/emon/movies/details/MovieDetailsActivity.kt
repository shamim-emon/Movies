package bd.emon.movies.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.movies.R
import bd.emon.movies.databinding.ActivityMovieDetailsBinding
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import bd.emon.movies.favourite.FavouriteStatusChangeListener
import bd.emon.movies.viewModels.DetailsViewModel
import bd.emon.movies.viewModels.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel
    private val args: MovieDetailsActivityArgs by navArgs()
    private lateinit var movieId: String
    private var isFav = false

    @Inject
    @ApiKey
    lateinit var apiKey: String

    @Inject
    @AppLanguage
    lateinit var language: String

    val showTrailer: (String) -> Unit = { url ->
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieId = args.movieId

        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        favouriteViewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        showLoader()
        makeApiCalls()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.backButtonTwo.setOnClickListener {
            finish()
        }

        detailsViewModel.movieDetails.observe(this) {
            binding.details = it
            binding.hasTransition = false
            hideLoader()
            showContentView()
            hideNoInternetView()
        }

        detailsViewModel.errorState.observe(this) {
            hideContentView()
            showNoInternetView()
            hideLoader()
        }

        detailsViewModel.movieVideos.observe(this) {
            when (it.results.isNotEmpty()) {
                true -> {
                    binding.cam.visibility = VISIBLE
                    binding.trailer.visibility = VISIBLE
                    binding.trailerList.adapter = TrailersAdapter(it.results, showTrailer)
                    binding.trailerList.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
                    binding.trailerList.isNestedScrollingEnabled = false
                }
                else -> {
                    binding.cam.visibility = INVISIBLE
                    binding.trailer.visibility = INVISIBLE
                }
            }
        }

        favouriteViewModel.getFavouriteMovieByIdState.observe(this) {
            it?.let {
                binding.fab.setImageResource(R.drawable.ic_fav_filled_48px)
                isFav = true
            } ?: run {
                binding.fab.setImageResource(R.drawable.ic_fav_48px)
                isFav = false
            }
        }

        favouriteViewModel.addToFavouriteState.observe(this) {
            binding.fab.setImageResource(R.drawable.ic_fav_filled_48px)
            isFav = true
            FavouriteStatusChangeListener.onChange()
        }

        favouriteViewModel.removeFromFavouriteState.observe(this) {
            binding.fab.setImageResource(R.drawable.ic_fav_48px)
            isFav = false
            FavouriteStatusChangeListener.onChange()
        }

        binding.fab.setOnClickListener {
            binding.details?.let { details ->
                val entity = MovieEntity(
                    id = details.id,
                    title = details.title,
                    poster_path = details.poster_path
                )

                when (isFav) {
                    true -> {
                        favouriteViewModel.removeFromFavourite(entity)
                    }
                    else -> {
                        favouriteViewModel.addToFavourite(entity)
                    }
                }
                favouriteViewModel.getFavMovieById(movieId.toInt())
            }
        }

        favouriteViewModel.errorState.observe(this) {
            hideLoader()
        }
    }

    fun makeApiCalls() {
        detailsViewModel.getMovieDetails(apiKey = apiKey, language = language, movieId = movieId)
        detailsViewModel.getMovieVideos(apiKey = apiKey, movieId = movieId)
        favouriteViewModel.getFavMovieById(movieId.toInt())
    }

    fun showNoInternetView() {
        binding.noInternetView.visibility = VISIBLE
    }

    fun hideNoInternetView() {
        binding.noInternetView.visibility = GONE
    }

    fun showContentView() {
        binding.contentView.visibility = VISIBLE
    }

    fun hideContentView() {
        binding.contentView.visibility = GONE
    }

    fun showLoader() {
        binding.progressBar.visibility = VISIBLE
    }

    fun hideLoader() {
        binding.progressBar.visibility = GONE
    }
}

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

    val showTrailer: (String) -> Unit = { url ->
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieId = args.movieId

        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        showLoader()
        makeApiCalls()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.backButtonTwo.setOnClickListener {
            finish()
        }

        viewModel.movieDetails.observe(this) {
            binding.details = it
            binding.hasTransition = false
            hideLoader()
            showContentView()
            hideNoInternetView()
        }

        viewModel.errorState.observe(this) {
            hideContentView()
            showNoInternetView()
            hideLoader()
        }

        viewModel.movieVideos.observe(this) {
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
    }

    fun makeApiCalls() {
        viewModel.getMovieDetails(apiKey = apiKey, language = language, movieId = movieId)
        viewModel.getMovieVideos(apiKey = apiKey, movieId = movieId)
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

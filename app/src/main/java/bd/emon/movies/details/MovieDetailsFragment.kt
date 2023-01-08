package bd.emon.movies.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import bd.emon.movies.R
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import bd.emon.movies.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private val args: MovieDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: DetailsViewModel

    @Inject
    @ApiKey
    lateinit var apiKey: String

    @Inject
    @AppLanguage
    lateinit var language: String

    private lateinit var movieId: String

    override fun showLoader() {
    }

    override fun hideLoader() {
    }

    override fun showNoInternetView() {
    }

    override fun hideNoInternetView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoader()
                else -> hideLoader()
            }
        }
        viewModel.movieDetails.observe(viewLifecycleOwner) {
        }
        movieId = args.movieId
        viewModel.getMovieDetails(apiKey = apiKey, language = language, movieId = movieId)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MovieDetailsFragment().apply {
            }
    }
}

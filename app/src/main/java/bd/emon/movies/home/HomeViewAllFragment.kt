package bd.emon.movies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import bd.emon.movies.MainActivity
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.dataMapper.DiscoverMovieMapper
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.common.paging.PagingHelper
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.common.view.ViewLoader
import bd.emon.movies.common.view.ViewLoaderImpl
import bd.emon.movies.common.view.ViewResizer
import bd.emon.movies.databinding.FragmentHomeViewAllBinding
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_GENRE_ID = "genreId"
private const val ARG_GENRE = "genre"

@AndroidEntryPoint
class HomeViewAllFragment : BaseFragment(), PagingHelper {
    private var genreId: Int? = null
    private var genre: String? = null
    private lateinit var binding: FragmentHomeViewAllBinding
    private lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var noInternetViewAssistedFactory: NoInternetViewAssistedFactory
    private lateinit var noInternetView: NoInternetView
    private lateinit var viewModel: HomeViewModel
    lateinit var viewLoaderImpl: ViewLoader
    private var discoverMovieApiPageNo = 1
    private var hasMoreData = true
    private lateinit var adapter: MovieListAdapter

    @Inject
    lateinit var mapper: DiscoverMovieMapper

    @Inject
    lateinit var viewResizer: ViewResizer

    override fun showLoader() {
        viewLoaderImpl.showLoader()
    }

    override fun hideLoader() {
        viewLoaderImpl.hideLoader()
    }

    override fun showNoInternetView() {
        noInternetView.layoutAndshowExceptionView()
    }

    override fun hideNoInternetView() {
        noInternetView.hideExceptionView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genreId = it.getInt(ARG_GENRE_ID)
            genre = it.getString(ARG_GENRE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeViewAllBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        screensNavigator = (activity as MainActivity).screensNavigator
        noInternetView = noInternetViewAssistedFactory.create(binding.exceptionView)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        binding.genre = genre
        binding.topAppBar.setNavigationOnClickListener {
            screensNavigator.back()
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    showLoader()
                }
                false -> {
                    hideLoader()
                }
            }
        }

        viewModel.discoverMoviesErrorState.observe(viewLifecycleOwner) {
            if (this::adapter.isInitialized) {
                showToast(requireContext(), NETWORK_ERROR_DEFAULT, Toast.LENGTH_LONG)
            } else {
                showNoInternetView()
            }
        }

        viewModel.discoverMovies.observe(
            viewLifecycleOwner
        ) {
            hideNoInternetView()
            if (discoverMovieApiPageNo == 1) {
                adapter =
                    MovieListAdapter(
                        mapper.mapFrom(it.results),
                        viewResizer,
                        true,
                        true,
                        this
                    )
                binding.movies.adapter = adapter
                binding.movies.layoutManager = GridLayoutManager(requireContext(), 2)
            } else {
                if (it.results.size > 0) {
                    adapter.addMoreMovies(mapper.mapFrom(it.results))
                } else {
                    hasMoreData = false
                    adapter.hideLoaders()
                }
            }

            discoverMovieApiPageNo++
        }

        viewModel.loadDiscoverFilters.observe(viewLifecycleOwner) {
            viewModel.apiParams[PARAM_GENRES] = genreId
            viewModel.loadDiscoverMovies(viewModel.apiParams, discoverMovieApiPageNo)
        }

        binding.swipeContainer.setOnRefreshListener {
            discoverMovieApiPageNo = 1
            hasMoreData = true
            viewModel.loadDiscoverMovies(viewModel.apiParams, discoverMovieApiPageNo)
        }

        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(genreId: Int, genre: String) =
            HomeViewAllFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_GENRE_ID, genreId)
                    putString(ARG_GENRE, genre)
                }
            }
    }

    override fun loadNextPage() {
        viewModel.loadDiscoverMovies(viewModel.apiParams, discoverMovieApiPageNo)
    }

    override fun hasMoreData(): Boolean {
        return hasMoreData
    }
}

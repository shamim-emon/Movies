package bd.emon.movies.movieEntity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.data.dataMapper.TrendingMovieMapper
import bd.emon.domain.INVALID_API_CALL_TYPE
import bd.emon.domain.NETWORK_ERROR_DEFAULT
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.paging.PagingHelper
import bd.emon.domain.view.NoInternetView
import bd.emon.domain.view.ViewLoader
import bd.emon.domain.view.ViewLoaderImpl
import bd.emon.domain.view.ViewSizeHelper
import bd.emon.movies.MainActivity
import bd.emon.movies.R
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.common.MovieDetailsNavigator
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.FragmentMovieEntityListBinding
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.home.MovieListAdapter
import bd.emon.movies.viewModels.HomeViewModel
import bd.emon.movies.viewModels.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_GENRE_ID = "genreId"
private const val ARG_PAGE_TITLE = "title"
private const val ARG_API_CALL_TYPE = "apiCallType"

@AndroidEntryPoint
class MovieEntityListFragment : BaseFragment(), PagingHelper, MovieDetailsNavigator {
    private val args: MovieEntityListFragmentArgs by navArgs()
    private var genreId: Int? = null
    private var pageTitle: String? = null
    private lateinit var binding: FragmentMovieEntityListBinding

    @Inject
    lateinit var noInternetViewAssistedFactory: NoInternetViewAssistedFactory
    private lateinit var noInternetView: NoInternetView
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var trendingViewModel: TrendingViewModel

    lateinit var viewLoaderImpl: ViewLoader
    private var apiPageNo = 1
    private var hasMoreData = true
    private lateinit var adapter: MovieListAdapter

    @Inject
    lateinit var discoverMovieMapper: DiscoverMovieMapper

    @Inject
    lateinit var trendingMovieMapper: TrendingMovieMapper

    @Inject
    lateinit var viewResizer: ViewSizeHelper

    @Inject
    @ApiKey
    lateinit var apiKey: String

    @Inject
    lateinit var navDirectionLabelProvider: MovieEntityNavDirectionLabelProvider

    lateinit var screensNavigator: ScreensNavigator

    lateinit var apiCallType: APICallType

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
            genreId = args.genreId
            pageTitle = args.pageTitle
            apiCallType = args.apiCallType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieEntityListBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        trendingViewModel = ViewModelProvider(this)[TrendingViewModel::class.java]

        screensNavigator = (activity as MainActivity).screensNavigator
        noInternetView = noInternetViewAssistedFactory.create(binding.exceptionView)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        binding.pageTitle = pageTitle
        binding.topAppBar.setNavigationOnClickListener {
            screensNavigator.navigateUp()
        }

        setNavIconByApiCalType(apiCallType)

        homeViewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    showLoader()
                }
                false -> {
                    hideLoader()
                }
            }
        }

        trendingViewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    showLoader()
                }
                false -> {
                    hideLoader()
                }
            }
        }

        homeViewModel.discoverMoviesErrorState.observe(viewLifecycleOwner) {
            onApiError()
        }

        trendingViewModel.trendingMoviesErrorState.observe(viewLifecycleOwner) {
            onApiError()
        }

        homeViewModel.discoverMovies.observe(
            viewLifecycleOwner
        ) {
            hideNoInternetView()
            if (apiPageNo == 1) {
                adapter =
                    MovieListAdapter(
                        discoverMovieMapper.mapFrom(it.results),
                        viewResizer,
                        true,
                        true,
                        this,
                        this
                    )
                binding.movies.adapter = adapter
                binding.movies.layoutManager = GridLayoutManager(requireContext(), 2)
            } else {
                if (it.results.size > 0) {
                    adapter.addMoreMovies(discoverMovieMapper.mapFrom(it.results))
                } else {
                    hasMoreData = false
                    adapter.hideLoaders()
                }
            }

            apiPageNo++
        }

        trendingViewModel.trendingMovies.observe(
            viewLifecycleOwner,
            Observer {
                hideNoInternetView()
                if (apiPageNo == 1) {
                    adapter =
                        MovieListAdapter(
                            trendingMovieMapper.mapFrom(it.results),
                            viewResizer,
                            true,
                            true,
                            this,
                            this
                        )
                    binding.movies.adapter = adapter
                    binding.movies.layoutManager = GridLayoutManager(requireContext(), 2)
                } else {
                    if (it.results.size > 0) {
                        adapter.addMoreMovies(trendingMovieMapper.mapFrom(it.results))
                    } else {
                        hasMoreData = false
                        adapter.hideLoaders()
                    }
                }

                apiPageNo++
            }
        )

        homeViewModel.loadDiscoverFilters.observe(viewLifecycleOwner) {
            homeViewModel.apiParams[PARAM_GENRES] = genreId
            makeApiCall(apiCallType)
        }

        binding.swipeContainer.setOnRefreshListener {
            apiPageNo = 1
            hasMoreData = true
            makeApiCall(apiCallType)
        }

        homeViewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onApiError() {
        if (this::adapter.isInitialized) {
            showToast(requireContext(), NETWORK_ERROR_DEFAULT, Toast.LENGTH_LONG)
        } else {
            showNoInternetView()
        }
    }

    private fun makeApiCall(apiCallType: APICallType) {
        when (apiCallType) {
            APICallType.DISCOVER_PAGING -> homeViewModel.loadDiscoverMovies(
                homeViewModel.apiParams,
                apiPageNo
            )
            APICallType.TRENDING_MOVIES -> trendingViewModel.loadTrendingMovies(apiKey, apiPageNo)
            else -> throw IllegalStateException(INVALID_API_CALL_TYPE)
        }
    }

    private fun setNavIconByApiCalType(apiCallType: APICallType) {
        when (apiCallType) {
            APICallType.DISCOVER_PAGING ->
                binding.topAppBar.navigationIcon =
                    resources.getDrawable(R.drawable.ic_arrow_back_24px)

            else -> binding.topAppBar.navigationIcon = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(genreId: Int, title: String, apiCallType: APICallType) =
            MovieEntityListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_GENRE_ID, genreId)
                    putString(ARG_PAGE_TITLE, title)
                    putSerializable(ARG_API_CALL_TYPE, apiCallType)
                }
            }

        @JvmStatic
        fun newInstance(title: String, apiCallType: APICallType) =
            MovieEntityListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PAGE_TITLE, title)
                    putSerializable(ARG_API_CALL_TYPE, apiCallType)
                }
            }
    }

    override fun loadNextPage() {
        makeApiCall(apiCallType)
    }

    override fun hasMoreData(): Boolean {
        return hasMoreData
    }

    override fun navigateToDetails(id: String) {
        val navDirectionLabel = navDirectionLabelProvider.getNavDirectionLabel(apiCallType)
        screensNavigator.navigateToMovieDetails(id, navDirectionLabel)
    }
}

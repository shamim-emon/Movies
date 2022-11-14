package bd.emon.movies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.MainActivity
import bd.emon.movies.R
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.menuItem.HomeMenuItemListener
import bd.emon.movies.common.menuItem.MenuItemListener
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.common.view.ViewLoader
import bd.emon.movies.common.view.ViewLoaderImpl
import bd.emon.movies.databinding.FragmentHomeBinding
import bd.emon.movies.di.assistedFactory.HomePatchAdapterAssistedFactory
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.movieEntity.APICallType
import bd.emon.movies.movieEntity.MovieEntityListFragment
import bd.emon.movies.viewModels.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class HomeFragment : BaseFragment(), HomeFragmentAdaptersCallBack {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomePatchesAdapter
    private var genres: List<Genre>? = null
    private lateinit var noInternetView: NoInternetView

    @Inject
    lateinit var noInternetViewAssistedFactory: NoInternetViewAssistedFactory

    @Inject
    lateinit var homePatchAdapterViewHolderFacade: HomePatchAdapterViewHolderFacade

    @Inject
    lateinit var homePatchAdapterAssistedFactory: HomePatchAdapterAssistedFactory

    @Inject
    lateinit var materialAlertDialogBuilder: Provider<MaterialAlertDialogBuilder>

    lateinit var viewLoaderImpl: ViewLoader

    lateinit var menuItemListener: MenuItemListener

    lateinit var filterDialogFacade: FilterDialogFacade
    lateinit var clearFilterDialog: ClearFilterDialog

    @Inject
    lateinit var sortingCriteria: List<String>

    @Inject
    lateinit var releaseYears: List<Int>

    private lateinit var orderByAdapterProvider: FilterDialogAdaptersProvider<String>
    private lateinit var yearAdapterProvider: FilterDialogAdaptersProvider<Int>

    private var discoverMovieApiPageNo = 1

    private lateinit var screensNavigator: ScreensNavigator

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        screensNavigator = (activity as MainActivity).screensNavigator

        noInternetView = noInternetViewAssistedFactory.create(binding.exceptionView)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        orderByAdapterProvider = FilterDialogAdaptersProvider(requireContext(), sortingCriteria)
        yearAdapterProvider = FilterDialogAdaptersProvider(requireContext(), releaseYears)
        filterDialogFacade = FilterDialogFacade(
            materialAlertDialogBuilder.get(),
            orderByAdapterProvider,
            yearAdapterProvider,
            viewModel,
            requireContext(),
            this
        )
        clearFilterDialog = ClearFilterDialog(
            homeViewModel = viewModel,
            requireContext(),
            materialAlertDialogBuilder.get()
        )
        menuItemListener = HomeMenuItemListener(filterDialogFacade, clearFilterDialog)
        viewModel.loadGenres(viewModel.apiParams)

        viewModel.genres.observe(
            viewLifecycleOwner
        ) {
            hideNoInternetView()
            if (this::adapter.isInitialized) {
                adapter.clearItems()
            }
            homePatchAdapterViewHolderFacade.clearAnyOnHoldData()
            genres = it.genres
            adapter = homePatchAdapterAssistedFactory.create(it.genres, this)
            binding.homeContents.adapter = adapter
            binding.homeContents.layoutManager = LinearLayoutManager(context)
        }

        viewModel.genreErrorState.observe(viewLifecycleOwner) {
            if (this::adapter.isInitialized) {
                showToast(
                    requireContext(),
                    text = resources.getString(R.string.no_internet_secondary_text),
                    Toast.LENGTH_LONG
                )
            } else {
                showNoInternetView()
            }
        }

        viewModel.discoverMoviesErrorState.observe(viewLifecycleOwner) {
            val viewHolder = homePatchAdapterViewHolderFacade.getViewHolder(it.grp_genre_id)
            homePatchAdapterViewHolderFacade.hideLoading(viewHolder)
            homePatchAdapterViewHolderFacade.handleViewHolderError(viewHolder)
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

        viewModel.discoverMovies.observe(
            viewLifecycleOwner
        ) {
            homePatchAdapterViewHolderFacade.inflateHomePatchViewHolder(it.grp_genre_id, it.results)
        }

        viewModel.discoverFiltersErrorState.observe(viewLifecycleOwner) {
            showToast(requireContext(), NETWORK_ERROR_DEFAULT, Toast.LENGTH_LONG)
        }

        viewModel.saveDiscoverFilters.observe(viewLifecycleOwner) {
            viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        }

        viewModel.loadDiscoverFilters.observe(viewLifecycleOwner) {
            viewModel.loadGenres(viewModel.apiParams)
        }

        viewModel.clearDiscoverFilters.observe(viewLifecycleOwner) {
            viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        }

        binding.swipeContainer.setOnRefreshListener {
            viewModel.loadGenres(viewModel.apiParams)
        }

        binding.topAppBar.setOnMenuItemClickListener {
            menuItemListener.handleClick(it)
            true
        }

        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun loadDiscoverItemByGenreId(genreId: Int) {
        val list = homePatchAdapterViewHolderFacade.getDistcoverListFromMap(genreId)
        list?.let {
            homePatchAdapterViewHolderFacade.inflateHomePatchViewHolder(
                genreId,
                list
            )
        } ?: run {
            val holder = homePatchAdapterViewHolderFacade.getViewHolder(genreId)
            homePatchAdapterViewHolderFacade.showLoading(holder)
            viewModel.apiParams[PARAM_GENRES] = genreId
            viewModel.loadDiscoverMovies(viewModel.apiParams, discoverMovieApiPageNo)
        }
    }

    override fun goToMovieEntityList(genreId: Int, genre: String) {
        val pageTitle = getString(R.string.genre_movies, genre)
        screensNavigator.goTo(
            MovieEntityListFragment.newInstance(
                genreId,
                pageTitle,
                APICallType.DISCOVER_PAGING
            )
        )
    }
}

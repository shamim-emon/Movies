package bd.emon.movies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.R
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.menuItem.MenuItemListener
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.common.view.ViewLoader
import bd.emon.movies.common.view.ViewLoaderImpl
import bd.emon.movies.databinding.FragmentHomeBinding
import bd.emon.movies.di.assistedFactory.HomeMenuItemListenerAssistedFactory
import bd.emon.movies.di.assistedFactory.HomePatchAdapterAssistedFactory
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
    lateinit var homeMenuItemListenerAssistedFactory: HomeMenuItemListenerAssistedFactory

    @Inject
    lateinit var homePatchAdapterAssistedFactory: HomePatchAdapterAssistedFactory

    lateinit var viewLoaderImpl: ViewLoader

    lateinit var menuItemListener: MenuItemListener

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
        noInternetView = noInternetViewAssistedFactory.create(binding.exceptionView)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        menuItemListener = homeMenuItemListenerAssistedFactory.create(
            viewModel,
            requireActivity()
        )

        viewModel.loadGenres(apiParams)

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

        binding.swipeContainer.setOnRefreshListener {
            viewModel.loadGenres(apiParams)
        }

        binding.topAppBar.setOnMenuItemClickListener {
            menuItemListener.handleClick(it)
            true
        }

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
            apiParams[PARAM_GENRES] = genreId
            viewModel.loadDiscoverMovies(apiParams)
        }
    }
}

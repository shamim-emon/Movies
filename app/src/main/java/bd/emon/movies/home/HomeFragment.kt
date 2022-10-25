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
import bd.emon.movies.common.view.NoContentView
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.common.view.ViewLoader
import bd.emon.movies.common.view.ViewLoaderImpl
import bd.emon.movies.databinding.FragmentHomeBinding
import bd.emon.movies.di.assistedFactory.HomePatchesAdapterAssistedFactory
import bd.emon.movies.di.assistedFactory.NoContentViewAssistedFactory
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(), DiscoverListAdapterCallBack {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomePatchesAdapter
    private lateinit var homePatchAdapterViewHolderContainer: HomePatchAdapterViewHolderContainer
    private var genres: List<Genre>? = null
    private lateinit var noInternetView: NoInternetView
    private lateinit var noContentView: NoContentView

    @Inject
    lateinit var homePatchesAdapteFactory: HomePatchesAdapterAssistedFactory

    @Inject
    lateinit var noInternetViewAssistedFactory: NoInternetViewAssistedFactory

    @Inject
    lateinit var noContentViewAssistedFactory: NoContentViewAssistedFactory

    lateinit var homePatchViewHolderHelper: HomePatchViewHolderHelper
    lateinit var discoverItemDataContainer: DiscoverItemDataContainer

    lateinit var viewLoaderImpl: ViewLoader

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
        homePatchViewHolderHelper = HomePatchViewHolderHelper()
        discoverItemDataContainer = DiscoverItemDataContainer()
        noInternetView = noInternetViewAssistedFactory.create(binding.exceptionView)
        noContentView = noContentViewAssistedFactory.create(binding.exceptionView)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.loadGenres(apiKey, language)

        viewModel.genres.observe(
            viewLifecycleOwner
        ) {
            hideNoInternetView()
            if (this::adapter.isInitialized) {
                adapter.clearItems()
            }
            genres = it.genres
            adapter = homePatchesAdapteFactory.create(genres!!, this)
            homePatchAdapterViewHolderContainer = adapter.getViewHoldersContainer()
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
            val viewHolder = homePatchAdapterViewHolderContainer.getViewHolder(it.grp_genre_id)
            homePatchViewHolderHelper.hideLoading(viewHolder)
            homePatchViewHolderHelper.handleViewHolderError(viewHolder)
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
            inflateHomePatchViewHolder(it.grp_genre_id, it.results)
        }

        binding.swipeContainer.setOnRefreshListener {
            viewModel.loadGenres(apiKey, language)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun loadDiscoverItemByGenreId(genreId: Int) {
        val list = discoverItemDataContainer.getDistcoverListFromMap(genreId)
        if (list == null) {
            showLoader(genreId)
            viewModel.loadDiscoverMovies(apiKey = apiKey, lang = language, genres = genreId)
        } else {
            inflateHomePatchViewHolder(
                genreId,
                list
            )
        }
    }

    override fun showLoader(genreId: Int) {
        val viewHolder = homePatchAdapterViewHolderContainer.getViewHolder(genreId)
        homePatchViewHolderHelper.showLoading(viewHolder)
    }

    private fun inflateHomePatchViewHolder(
        genreId: Int,
        list: MutableList<bd.emon.movies.entity.discover.Result>
    ) {
        val viewHolder = homePatchAdapterViewHolderContainer.getViewHolder(genreId)
        homePatchViewHolderHelper.hideLoading(viewHolder)
        discoverItemDataContainer.addDiscoverListToMap(genreId, list)
        homePatchViewHolderHelper.handleViewHolderSuccess(viewHolder, list)
    }
}

package bd.emon.movies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.databinding.FragmentHomeBinding
import bd.emon.movies.di.assistedFactory.HomePatchesAdapteFactory
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(), DiscoverListAdapterCallBack {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var adapter: HomePatchesAdapter? = null
    private lateinit var homePatchAdapterViewHolderContainer: HomePatchAdapterViewHolderContainer
    private var genres: List<Genre>? = null

    @Inject
    lateinit var homePatchesAdapteFactory: HomePatchesAdapteFactory

    @Inject
    lateinit var homePatchViewHolderHelper: HomePatchViewHolderHelper

    override fun showLoader() {
        binding.swipeContainer.isRefreshing = true
    }

    override fun hideLoader() {
        binding.swipeContainer.isRefreshing = false
    }

    override fun showNoInternetView() {
        binding.noInternetView.root.visibility = VISIBLE
    }

    override fun hideNoInternetView() {
        binding.noInternetView.root.visibility = GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.noInternetView.root.visibility = GONE
        viewModel.loadGenres(apiKey, language)

        viewModel.genres.observe(
            viewLifecycleOwner
        ) {
            hideNoInternetView()
            genres = it.genres
            adapter = homePatchesAdapteFactory.create(genres!!, this)
            homePatchAdapterViewHolderContainer = adapter!!.getDiscoverListAdapterContainer()
            binding.homeContents.adapter = adapter
            binding.homeContents.layoutManager = LinearLayoutManager(context)
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            if (adapter == null || adapter!!.isEmpty()) {
                showNoInternetView()
            } else {
                hideNoInternetView()
            }
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
            val viewHolder = homePatchAdapterViewHolderContainer.getViewHolder(it.grp_genre_id)
            homePatchViewHolderHelper.handleViewHolder(viewHolder, it.results)
        }

        binding.swipeContainer.setOnRefreshListener {
            adapter?.clearItems()
            viewModel.loadGenres(apiKey, language)
        }

        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun loadDiscoverItemByGenreId(genreId: Int) {
        viewModel.loadDiscoverMovies(apiKey = apiKey, lang = language, genres = genreId)
    }
}

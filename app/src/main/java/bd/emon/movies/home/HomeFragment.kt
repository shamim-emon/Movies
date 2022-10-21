package bd.emon.movies.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
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
class HomeFragment : BaseFragment(),DiscoverListAdapterCallBack {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomePatchesAdapter
    private lateinit var discoverListAdaptersContainer: DiscoverListAdaptersContainer
    private lateinit  var genres:List<Genre>

    @Inject
    lateinit var homePatchesAdapteFactory: HomePatchesAdapteFactory

    override fun showLoader() {
        binding.loader.visibility = VISIBLE
    }

    override fun hideLoader() {
        binding.loader.visibility = GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.loadGenres(apiKey, language)

        viewModel.genres.observe(
            viewLifecycleOwner
        ) {
            genres=it.genres
            adapter = homePatchesAdapteFactory.create(genres,this)
            discoverListAdaptersContainer=adapter.getDiscoverListAdapterContainer()
            binding.homeContents.adapter = adapter
            binding.homeContents.layoutManager = LinearLayoutManager(context)

        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            showToast(requireContext(), it.message!!)
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
            Log.e("Genre","$ M->${it.grp_genre_id}")
            discoverListAdaptersContainer.getAdapterFromContainer(it.grp_genre_id)?.populateList(it.results.toMutableList())

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }

    override fun loadDiscoverItemByGenreId(genreId: Int) {
        viewModel.loadDiscoverMovies(apiKey = apiKey, lang = language, genres = genreId)
    }
}


package bd.emon.movies.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import bd.emon.movies.base.BaseFragment
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.dataMapper.SearchMovieMapper
import bd.emon.movies.common.paging.PagingHelper
import bd.emon.movies.common.view.NoContentView
import bd.emon.movies.common.view.NoInternetView
import bd.emon.movies.common.view.ViewLoader
import bd.emon.movies.common.view.ViewLoaderImpl
import bd.emon.movies.common.view.ViewResizer
import bd.emon.movies.databinding.FragmentSearchBinding
import bd.emon.movies.di.assistedFactory.NoContentViewAssistedFactory
import bd.emon.movies.di.assistedFactory.NoInternetViewAssistedFactory
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import bd.emon.movies.home.MovieListAdapter
import bd.emon.movies.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment(), PagingHelper {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewLoaderImpl: ViewLoader
    private lateinit var noInternetView: NoInternetView
    private lateinit var noContentView: NoContentView

    @Inject
    lateinit var viewResizer: ViewResizer
    private lateinit var adapter: MovieListAdapter

    @Inject
    lateinit var noInternetViewAssistedFactory: NoInternetViewAssistedFactory

    @Inject
    lateinit var noContentViewAssistedFactory: NoContentViewAssistedFactory

    @Inject
    lateinit var searchMovieMapper: SearchMovieMapper

    @Inject
    @ApiKey
    lateinit var apiKey: String

    @Inject
    @AppLanguage
    lateinit var language: String

    private var pageNo: Int = 1
    private var hasMoreData = true

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

    fun showNoContentView() {
        noContentView.layoutAndshowExceptionView()
    }

    fun hideNoContentView() {
        noContentView.hideExceptionView()
    }

    fun showSuggestionView() {
        binding.suggestionView.visibility = VISIBLE
    }

    fun hideSuggestionView() {
        binding.suggestionView.visibility = GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewLoaderImpl = ViewLoaderImpl(binding.swipeContainer)
        noInternetView = noInternetViewAssistedFactory.create(binding.noInternetView)
        noContentView = noContentViewAssistedFactory.create(binding.noContentView)

        binding.searchView?.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    keyboardHelper.hideKeyboard(binding.searchView)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    when (newText.toString().length > 2) {
                        true -> hideSuggestionView()
                        else -> showSuggestionView()
                    }
                    resetViews()
                    makeApiCall()
                    return false
                }
            })
        binding.swipeContainer.setOnRefreshListener {
            resetViews()
            makeApiCall()
        }
        viewModel.movieSearch.observe(viewLifecycleOwner) {
            hideLoader()
            if (pageNo == 1) {
                if (it.results.size > 0) {
                    adapter =
                        MovieListAdapter(
                            searchMovieMapper.mapFrom(it.results),
                            viewResizer,
                            true,
                            true,
                            this
                        )
                    binding.searchedContents.adapter = adapter
                    binding.searchedContents.layoutManager = GridLayoutManager(requireContext(), 2)
                } else {
                    showNoContentView()
                }
            } else {
                if (it.results.size > 0) {
                    adapter.addMoreMovies(searchMovieMapper.mapFrom(it.results))
                } else {
                    hasMoreData = false
                    adapter.hideLoaders()
                }
            }
            pageNo++
        }
        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoader()
                else -> hideLoader()
            }
        }
        viewModel.errorState.observe(viewLifecycleOwner) {
            hideLoader()
            when (pageNo) {
                1 -> {
                    showNoInternetView()
                }
                else -> showToast(requireContext(), NETWORK_ERROR_DEFAULT, Toast.LENGTH_LONG)
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
            }
    }

    override fun loadNextPage() {
        makeApiCall()
    }

    override fun hasMoreData() = hasMoreData

    fun makeApiCall() {
        viewModel.searchMovie(
            apiKey = apiKey,
            language = language,
            page = pageNo,
            includeAdult = true,
            query = binding.searchView.query.toString() ?: ""
        )
    }

    fun resetViews() {
        hideLoader()
        hasMoreData = true
        pageNo = 1
        if (this::adapter.isInitialized) {
            adapter.clearItems()
        }
        hideNoContentView()
        hideNoInternetView()
    }
}

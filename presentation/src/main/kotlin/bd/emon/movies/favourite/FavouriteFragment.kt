package bd.emon.movies.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import bd.emon.domain.DB_TRANSACTION_ERROR
import bd.emon.domain.navigation.NavDirectionLabel
import bd.emon.domain.view.ViewSizeHelper
import bd.emon.movies.MainActivity
import bd.emon.movies.common.MovieDetailsNavigator
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.FragmentFavouriteBinding
import bd.emon.movies.home.MovieListAdapter
import bd.emon.movies.viewModels.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : Fragment(), MovieDetailsNavigator, FavouriteStatusChangeSubscriber {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var adapter: MovieListAdapter
    private lateinit var binding: FragmentFavouriteBinding

    @Inject
    lateinit var viewResizer: ViewSizeHelper

    fun showLoader() {
        binding.progressBar.visibility = VISIBLE
    }

    fun hideLoader() {
        binding.progressBar.visibility = GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        FavouriteStatusChangeListener.addSubscriber(this)
        screensNavigator = (activity as MainActivity).screensNavigator
        favouriteViewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]

        favouriteViewModel.errorState.observe(viewLifecycleOwner) {
            hideLoader()
            showToast(requireContext(), DB_TRANSACTION_ERROR, Toast.LENGTH_LONG)
        }

        favouriteViewModel.getAllFavouriteState.observe(
            viewLifecycleOwner
        ) {
            when (it.isNotEmpty()) {
                true -> {
                    binding.exceptionView.root.visibility = GONE
                    adapter = MovieListAdapter(
                        it.toMutableList(), viewResizer, true, false, null, this
                    )
                    binding.movies.adapter = adapter
                    binding.movies.layoutManager = GridLayoutManager(requireContext(), 2)
                }
                false -> {
                    binding.exceptionView.root.visibility = VISIBLE
                }
            }

            hideLoader()
        }

        showLoader()
        favouriteViewModel.getAllFavs()

        return binding.root
    }

    override fun onDestroy() {
        FavouriteStatusChangeListener.removeSubcriber()
        super.onDestroy()
    }

    override fun navigateToDetails(id: String) {
        screensNavigator.navigateToMovieDetails(id, NavDirectionLabel.FavouriteFragment)
    }

    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, duration).show()
    }

    override fun reloadFavourites() {
        adapter.clearItems()
        favouriteViewModel.getAllFavs()
    }
}

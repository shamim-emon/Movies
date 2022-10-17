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
import bd.emon.movies.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomePatchesAdapter
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
            adapter = HomePatchesAdapter(it.genres)
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
            viewLifecycleOwner,
            Observer {
                Log.e("DMs", "${it.results}")
            }
        )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}

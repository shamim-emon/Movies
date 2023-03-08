package bd.emon.movies.home

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding
import bd.emon.movies.di.entryPoint.FragmentEntryPoint
import bd.emon.movies.viewModels.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.EntryPointAccessors

class FilterDialogFacade(
    private val materialAlertDialogBuilder: MaterialAlertDialogBuilder,
    private val orderByAdapterProvider: FilterDialogAdaptersProvider<String>,
    private val yearAdapterProvider: FilterDialogAdaptersProvider<Int>,
    private val homeViewModel: HomeViewModel,
    private val context: Context,
    private val fragment: Fragment
) : Dialog {
    private val fragmentEntryPoint =
        EntryPointAccessors.fromFragment(fragment, FragmentEntryPoint::class.java)

    private var bindingHelper: FilterDialogBindingHelper =
        fragmentEntryPoint.filterDialogBindingHelper()

    private lateinit var binding: LayoutDiscoverMovieFilterBinding

    private val orderByAdapter = orderByAdapterProvider.getAdapter()
    private val yearAdapter = yearAdapterProvider.getAdapter()

    override fun createAndDisplayDialog() {
        binding = LayoutDiscoverMovieFilterBinding.inflate(
            LayoutInflater.from(context)
        )

        binding.spinnerOrderBy.adapter = orderByAdapter
        binding.spinnerReleaseYear.adapter = yearAdapter

        bindingHelper.apply {
            setBinding(binding)
            setMinVoteUpperLimit()
            setMinVoteValueChangeListener()
            setData(homeViewModel.apiParams)
        }

        materialAlertDialogBuilder.setView(binding.root)
            .setPositiveButton(context.getString(R.string.save)) { dialog, which ->

                val map = bindingHelper.updateApiParam(homeViewModel.apiParams)
                homeViewModel.saveDiscoverMoviesFilters(
                    minVoteCount = map[PARAM_VOTE_COUNT_GREATER_THAN] as Int,
                    includeAdultContent = map[PARAM_INCLUDE_ADULT] as Boolean,
                    orderBy = map[PARAM_SORT_BY] as String,
                    releaseYearStr = "${map[PARAM_RELEASE_YEAR] as Int}"
                )
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
                // Respond to positive button press
            }
            .setTitle(R.string.filters)
            .show()
    }
}

package bd.emon.movies.home

import android.content.Context
import android.view.LayoutInflater
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding
import bd.emon.movies.viewModels.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FilterDialogFacade(
    private val materialAlertDialogBuilder: MaterialAlertDialogBuilder,
    private val apiParams: HashMap<String, Any?>,
    private val orderByAdapterProvider: FilterDialogAdaptersProvider<String>,
    private val yearAdapterProvider: FilterDialogAdaptersProvider<Int>,
    private val homeViewModel: HomeViewModel,
    private val context: Context
) {
    private lateinit var binding: LayoutDiscoverMovieFilterBinding

    private val orderByAdapter = orderByAdapterProvider.getAdapter()
    private val yearAdapter = yearAdapterProvider.getAdapter()
    private lateinit var builder: MaterialAlertDialogBuilder

    fun createAndDisplayDialog() {
        binding = LayoutDiscoverMovieFilterBinding.inflate(
            LayoutInflater.from(context)
        )
        binding.spinnerOrderBy.adapter = orderByAdapter
        binding.spinnerReleaseYear.adapter = yearAdapter
        builder = materialAlertDialogBuilder.setView(binding.root)
            .setPositiveButton(context.getString(R.string.save)) { dialog, which ->
                // Respond to positive button press
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
                // Respond to positive button press
            }
            .setTitle(R.string.filters)
        builder.show()
    }
}

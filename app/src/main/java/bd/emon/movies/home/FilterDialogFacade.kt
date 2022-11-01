package bd.emon.movies.home

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding
import bd.emon.movies.viewModels.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class FilterDialogFacade @AssistedInject constructor(
    private val materialAlertDialogBuilder: MaterialAlertDialogBuilder,
    @Assisted private val apiParams: HashMap<String, Any?>,
    @Assisted private val orderByAdapterProvider: FilterDialogAdaptersProvider<String>,
    @Assisted private val yearAdapterProvider: FilterDialogAdaptersProvider<Int>,
    @Assisted private val homeViewModel: HomeViewModel,
    @Assisted private val context: Context
) {
    private val binding: LayoutDiscoverMovieFilterBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_discover_movie_filter,
        null,
        false
    )

    private val orderByAdapter = orderByAdapterProvider.getAdapter()
    private val yearAdapter = yearAdapterProvider.getAdapter()
    private var builder: MaterialAlertDialogBuilder

    init {
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
    }

    fun show() {
        builder.show()
    }
}

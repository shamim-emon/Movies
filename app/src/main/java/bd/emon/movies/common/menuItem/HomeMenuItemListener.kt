package bd.emon.movies.common.menuItem

import android.app.Activity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding
import bd.emon.movies.viewModels.HomeViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class HomeMenuItemListener @AssistedInject constructor(
    @Assisted private val homeViewModel: HomeViewModel,
    @Assisted private val activity: Activity,
    private val sortingCriteriaList: List<String>,
    private val releaseYearList: List<Int>
) :
    MenuItemListener(activity) {

    override fun handleClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.filter -> showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val binding: LayoutDiscoverMovieFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.layout_discover_movie_filter,
            null,
            false
        )

        val orderByAdapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, sortingCriteriaList)
        val yearAdapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, releaseYearList)
        binding.spinnerOrderBy.adapter = orderByAdapter
        binding.spinnerReleaseYear.adapter = yearAdapter
        materialAlertDialogBuilder.setView(binding.root)
            .setPositiveButton(activity.getString(R.string.save)) { dialog, which ->
                // Respond to positive button press
            }
            .setNegativeButton(activity.getString(R.string.cancel)) { dialog, which ->
                // Respond to positive button press
            }
            .setTitle(R.string.filters)
            .show()
    }
}

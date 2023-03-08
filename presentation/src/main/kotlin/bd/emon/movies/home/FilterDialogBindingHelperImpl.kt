package bd.emon.movies.home

import android.widget.ArrayAdapter
import android.widget.SeekBar
import bd.emon.domain.DEFAULT_MINIMUM_VOTE_COUNT
import bd.emon.domain.DESC
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.databinding.LayoutDiscoverMovieFilterBinding

class FilterDialogBindingHelperImpl : FilterDialogBindingHelper {

    private lateinit var binding: LayoutDiscoverMovieFilterBinding

    override fun setBinding(binding: LayoutDiscoverMovieFilterBinding) {
        this.binding = binding
    }

    override fun setMinVoteUpperLimit() {
        binding.seekBar.max = DEFAULT_MINIMUM_VOTE_COUNT
    }

    override fun setMinVoteValueChangeListener() {
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.voteCount.text = "${binding.seekBar.progress}"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }
            }
        )
    }

    override fun setData(map: HashMap<String, Any?>) {
        binding.voteCount.text = map[PARAM_VOTE_COUNT_GREATER_THAN].toString()
        binding.seekBar.progress = map[PARAM_VOTE_COUNT_GREATER_THAN] as Int

        val orderByAdapter = binding.spinnerOrderBy.adapter as ArrayAdapter<String>
        var orderBy = (map[PARAM_SORT_BY] as String).replace(".$DESC", "")
        binding.spinnerOrderBy.setSelection(orderByAdapter.getPosition(orderBy))

        map[PARAM_RELEASE_YEAR]?.let {
            val releaseYearAdapter = binding.spinnerReleaseYear.adapter as ArrayAdapter<Int>

            binding.spinnerReleaseYear.setSelection(releaseYearAdapter.getPosition((it as String).toInt()))
        }
    }

    override fun updateApiParam(map: HashMap<String, Any?>): HashMap<String, Any?> {
        map[PARAM_VOTE_COUNT_GREATER_THAN] = binding.voteCount.text.toString().toInt()
        map[PARAM_SORT_BY] = binding.spinnerOrderBy.selectedItem.toString()
        map[PARAM_RELEASE_YEAR] = binding.spinnerReleaseYear.selectedItem
        return map
    }
}

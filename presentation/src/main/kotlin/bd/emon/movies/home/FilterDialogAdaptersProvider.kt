package bd.emon.movies.home

import android.content.Context
import android.widget.ArrayAdapter

class FilterDialogAdaptersProvider<T>(
    private val context: Context,
    private val list: List<T>
) {

    private val layout = android.R.layout.simple_spinner_item
    fun getAdapter(): ArrayAdapter<T> {
        return ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    }
}

package bd.emon.movies.common

import android.view.View
import android.view.inputmethod.InputMethodManager
import javax.inject.Inject

class KeyboardHelper @Inject constructor(private val manager: InputMethodManager) {

    fun hideKeyboard(focusedView: View) {
        manager.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}

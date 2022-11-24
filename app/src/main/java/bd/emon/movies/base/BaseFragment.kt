package bd.emon.movies.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import bd.emon.movies.common.KeyboardHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var keyboardHelper: KeyboardHelper
    abstract fun showLoader()
    abstract fun hideLoader()
    abstract fun showNoInternetView()
    abstract fun hideNoInternetView()
    protected fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }
}

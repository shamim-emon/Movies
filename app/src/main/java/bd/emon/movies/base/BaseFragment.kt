package bd.emon.movies.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var apiParams: HashMap<String, Any?>
    abstract fun showLoader()
    abstract fun hideLoader()
    abstract fun showNoInternetView()
    abstract fun hideNoInternetView()
    protected fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }
}

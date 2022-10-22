package bd.emon.movies.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    @Inject
    @ApiKey
    protected lateinit var apiKey: String

    @Inject
    @AppLanguage
    protected lateinit var language: String

    abstract fun showLoader()
    abstract fun hideLoader()
    abstract fun showNoInternetView()
    abstract fun hideNoInternetView()
    protected fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }
}

package bd.emon.domain.view

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutExceptionBinding
import bd.emon.movies.di.qualifier.NoContent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

@NoContent
class NoContentView @AssistedInject constructor(
    @Assisted private val binding: LayoutExceptionBinding,
    @ApplicationContext private val context: Context
) :
    ExceptionView {

    override fun layoutAndshowExceptionView() {
        binding.icon.setImageDrawable(context.resources.getDrawable(R.drawable.nothing_found))
        binding.pageTitle.text = context.resources.getString(R.string.no_result_primary_text)
        binding.pageSecondaryTitle.text =
            context.resources.getString(R.string.no_result_secondary_text)
        binding.root.visibility = VISIBLE
    }

    override fun hideExceptionView() {
        binding.root.visibility = GONE
    }
}

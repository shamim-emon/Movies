package bd.emon.movies.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bd.emon.domain.entity.genre.Genre
import bd.emon.movies.R
import bd.emon.movies.common.getGenreFromId
import bd.emon.movies.databinding.LayoutVerticalRecyclerViewBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class HomePatchesAdapter @AssistedInject constructor(
    @Assisted private var genres: List<Genre>,
    @Assisted private val callBack: HomeFragmentAdaptersCallBack,
    private val homePatchAdapterViewHolderFacade: HomePatchAdapterViewHolderFacade
) :
    RecyclerView.Adapter<HomePatchesAdapter.ViewHolder>(), HomePatchesAdapterHelper {

    inner class ViewHolder(var binding: LayoutVerticalRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var tag: Int? = null

        init {
            binding.seeAll.setOnClickListener {
                tag?.let { genreId ->
                    val genre = genres.getGenreFromId(genreId)
                    callBack.seeMore(genreId, genre)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutVerticalRecyclerViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_vertical_recycler_view,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.genre = genres[position]
        homePatchAdapterViewHolderFacade.addViewHolder(genres[position].id, holder)
        callBack.loadDiscoverItemByGenreId(genres[position].id)
    }

    override fun getItemCount() = genres.size

    //region homePatchesAdapterHelper methods
    override fun clearItems() {
        genres = mutableListOf()
        notifyDataSetChanged()
    }

    override fun isEmpty() = genres.isEmpty()
//endregion
}

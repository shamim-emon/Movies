package bd.emon.movies.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import bd.emon.domain.DEFAULT_VIEW_RESIZE_MARGIN
import bd.emon.domain.INVALID_VIEW_HOLDER
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.paging.PagingHelper
import bd.emon.domain.view.ViewSizeHelper
import bd.emon.movies.R
import bd.emon.movies.common.MovieDetailsNavigator
import bd.emon.movies.common.hide
import bd.emon.movies.databinding.LayoutLoaderBinding
import bd.emon.movies.databinding.LayoutMovieEntityBinding

class MovieListAdapter(
    private var movies: MutableList<MovieEntity>,
    private val viewResizer: ViewSizeHelper,
    private val shouldResize: Boolean = false,
    private val pagingEnabled: Boolean = false,
    private val pagingHelper: PagingHelper? = null,
    private val movieDetailsNavigator: MovieDetailsNavigator? = null
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private val MOVIE_ENTITY = 1
    private val LOADER_VIEW = 2

    inner class ViewHolder :
        RecyclerView.ViewHolder {
        var movieBinding: LayoutMovieEntityBinding? = null
        var loaderBinding: LayoutLoaderBinding? = null

        constructor(binding: LayoutMovieEntityBinding) : super(binding.root) {
            movieBinding = binding
            if (shouldResize) {
                viewResizer.makeViewHalfScreenWidth(binding.thumbnail, DEFAULT_VIEW_RESIZE_MARGIN)
            }
            movieBinding?.root?.setOnClickListener {
                if (adapterPosition != -1) {
                    movieDetailsNavigator?.navigateToDetails(movies[adapterPosition].idString)
                }
            }
        }

        constructor(binding: LayoutLoaderBinding) : super(binding.root) {
            loaderBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewBinding
        when (viewType) {
            MOVIE_ENTITY -> {
                binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_movie_entity,
                    parent,
                    false
                )
                return ViewHolder(binding as LayoutMovieEntityBinding)
            }
            LOADER_VIEW -> {
                binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_loader,
                    parent,
                    false
                )
                return ViewHolder(binding as LayoutLoaderBinding)
            }
            else -> throw IllegalStateException(INVALID_VIEW_HOLDER)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieBinding?.let {
            it.movie = movies[position]
        }
        holder.loaderBinding?.let {
            if (!pagingHelper!!.hasMoreData()) {
                it.hide()
            }
            if (pagingHelper!!.hasMoreData() && position == movies.size) {
                pagingHelper.loadNextPage()
            }
        }
    }

    override fun getItemCount(): Int {
        if (pagingEnabled) {
            return movies.size + 2
        }
        return movies.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position > movies.size - 1) {
            return LOADER_VIEW
        }
        return MOVIE_ENTITY
    }

    fun addMoreMovies(entities: MutableList<MovieEntity>) {
        var notifyStartIndex = movies.size
        movies.addAll(entities)
        notifyItemRangeChanged(notifyStartIndex, entities.size)
    }

    fun hideLoaders() {
        notifyItemRangeChanged(movies.size, 2)
    }

    fun clearItems() {
        movies = mutableListOf()
        notifyDataSetChanged()
    }
}

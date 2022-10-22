package bd.emon.movies.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutDiscoverItemBinding
import bd.emon.movies.entity.discover.Result

class DiscoverListAdapter(val genre: String) : RecyclerView.Adapter<DiscoverListAdapter.ViewHolder>() {

    private var movies: MutableList<Result> = mutableListOf()

    inner class ViewHolder(val binding: LayoutDiscoverItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutDiscoverItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_discover_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.movie = movies[position]
    }

    override fun getItemCount() = movies.size

    fun populateList(movies: MutableList<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}

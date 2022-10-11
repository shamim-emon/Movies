package bd.emon.movies.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutVerticalRecyclerViewBinding
import bd.emon.movies.entity.genre.Genre

class HomePatchesAdapter(private val genres: List<Genre>) :
    RecyclerView.Adapter<HomePatchesAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: LayoutVerticalRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutVerticalRecyclerViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_vertical_recycler_view,
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.genre=genres[position]
    }

    override fun getItemCount()=genres.size
}
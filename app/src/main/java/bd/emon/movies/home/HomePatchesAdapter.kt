package bd.emon.movies.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutVerticalRecyclerViewBinding
import bd.emon.movies.entity.genre.Genre
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class HomePatchesAdapter @AssistedInject constructor(@Assisted private val genres: List<Genre>,@Assisted private val callBack: DiscoverListAdapterCallBack ,private val discoverListAdapterContainer: DiscoverListAdaptersContainer = DiscoverListAdaptersContainerImpl()) :
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
        val adapter:DiscoverListAdapter = discoverListAdapterContainer.getAdapterFromContainer(genres[position].id)?:DiscoverListAdapter(genres[position].name)
        holder.binding.list.adapter=adapter
        holder.binding.list.layoutManager =LinearLayoutManager(holder.binding.list.context,LinearLayoutManager.HORIZONTAL, false)
        discoverListAdapterContainer.addAdapterToContainer(genres[position].id, adapter)
        Log.e("Genre","$ Calling ->${genres[position].name} - ${genres[position].id}")
        callBack.loadDiscoverItemByGenreId(genres[position].id)
    }

    override fun getItemCount()=genres.size

    fun getDiscoverListAdapterContainer()=discoverListAdapterContainer
}
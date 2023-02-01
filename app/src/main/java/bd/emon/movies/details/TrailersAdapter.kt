package bd.emon.movies.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutTrailerBinding
import bd.emon.movies.entity.details.Result

class TrailersAdapter(val results: List<Result>? = listOf()) :
    RecyclerView.Adapter<TrailersAdapter.ViewHolder>() {

    inner class ViewHolder : RecyclerView.ViewHolder {
        var binding: LayoutTrailerBinding

        constructor(binding: LayoutTrailerBinding) : super(binding.root) {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: LayoutTrailerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_trailer,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        results?.get(position)?.let {
            holder.binding.trailer = it
        }
    }

    override fun getItemCount() = results?.size ?: 0
}

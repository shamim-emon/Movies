package bd.emon.movies.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bd.emon.domain.entity.details.Result
import bd.emon.movies.R
import bd.emon.movies.databinding.LayoutTrailerBinding

class TrailersAdapter(
    private val results: List<Result>? = listOf(),
    private val action: (String) -> Unit
) :
    RecyclerView.Adapter<TrailersAdapter.ViewHolder>() {

    inner class ViewHolder : RecyclerView.ViewHolder {
        var binding: LayoutTrailerBinding

        constructor(binding: LayoutTrailerBinding) : super(binding.root) {
            this.binding = binding
            this.binding.root.setOnClickListener {
                if (adapterPosition != -1) {
                    action(results!![adapterPosition].url)
                }
            }
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

package bd.emon.movies.imageLoader

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import bd.emon.movies.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object ImageLoader {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .transform(CenterCrop(), RoundedCorners(8))
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .placeholder(R.drawable.place_holder_potrait)
            .error(R.drawable.error_potrait)
            .into(imageView)
    }
}

package bd.emon.movies.imageLoader

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageLoader {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView,imageUrl:String){
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }
}
package bd.emon.domain.entity.common

import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.emon.domain.IMAGE_BASE_URL

@Entity
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String
) {
    val imageUrl: String
        get() = IMAGE_BASE_URL + poster_path

    val idString: String
        get() = id.toString()
}

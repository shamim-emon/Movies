package bd.emon.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieEntity): Single<Long>

    @Delete
    fun deleteMovie(movie: MovieEntity): Single<Int>

    @Query("SELECT * FROM MovieEntity")
    fun getAll(): Single<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE id =:movieId")
    fun getFavouriteMovieById(movieId: Int): Single<MovieEntity>
}

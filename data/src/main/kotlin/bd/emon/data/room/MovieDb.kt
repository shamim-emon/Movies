package bd.emon.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bd.emon.domain.MovieEntityDao
import bd.emon.domain.entity.common.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieEntityDao

    companion object {
        const val dbName = "movies.db"

        @Volatile
        private var instance: MovieDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            MovieDb::class.java,
            dbName
        )
            .build()
    }
}
